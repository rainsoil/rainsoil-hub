package com.rainsoil.common.framework.threadpool;

import cn.hutool.core.lang.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainsoil.common.framework.threadpool.config.DynamicThreadPoolProperties;
import com.rainsoil.common.framework.threadpool.config.ThreadPoolProperties;
import com.rainsoil.common.framework.threadpool.enums.QueueTypeEnum;
import com.rainsoil.common.framework.threadpool.enums.RejectedExecutionHandlerEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 动态线程池管理器
 *
 * @author luyanan
 * @since 2021/8/19
 **/
@Slf4j
public class DynamicThreadPoolManager {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DynamicThreadPoolProperties dynamicThreadPoolProperties;

	/**
	 * 存储线程池对象, key->名称,value-> 对象
	 *
	 * @since 2021/8/20
	 */

	@Getter
	private Map<String, DynamicThreadPoolExecutor> threadPoolExecutorMap = new HashMap<>();

	/**
	 * 存储线程拒绝次数, key->名称,value-> 次数
	 *
	 * @since 2021/8/20
	 */

	private static Map<String, AtomicLong> THREAD_POOL_EXECUTOR_REJECT_COUNT_MAP = new ConcurrentHashMap<>();

	/**
	 * 初始化方法
	 *
	 * @since 2022/3/3
	 */
	@PostConstruct
	public void init() {
		createThreadPoolExecutor(dynamicThreadPoolProperties);
	}

	/**
	 * 创建线程池
	 *
	 * @param threadPoolName  线程池名称
	 * @param corePoolSize    核心线程池
	 * @param maximumPoolSize 最大线程池
	 * @param keepAliveTime   线程存活时间
	 * @param unit            单位
	 * @param workQueue       队列
	 * @return DynamicThreadPoolExecutor
	 * @since 2021/8/20
	 */
	public DynamicThreadPoolExecutor createThreadPoolExecutor(String threadPoolName, int corePoolSize,
															  int maximumPoolSize, long keepAliveTime,
															  TimeUnit unit, BlockingQueue<Runnable> workQueue) {

		return createThreadPoolExecutor(threadPoolName,
				corePoolSize,
				maximumPoolSize,

				keepAliveTime,
				unit,
				workQueue,
				null,
				null);
	}

	/**
	 * 创建线程执行器
	 *
	 * @param threadPoolName 线程池名称
	 * @return com.rainsoil.common.framework.threadpool.DynamicThreadPoolExecutor
	 * @since 2022/3/3
	 */
	public DynamicThreadPoolExecutor createThreadPoolExecutor(String threadPoolName) {
		return createThreadPoolExecutor(threadPoolName,
				1,
				Integer.MAX_VALUE,
				60,
				TimeUnit.SECONDS,
				new LinkedBlockingDeque<>(),
				new DynamicThreadFactory(threadPoolName),
				new DynamicAbortPolicy(threadPoolName));
	}

	/**
	 * 创建线程池
	 *
	 * @param threadPoolName           线程池名称
	 * @param corePoolSize             核心线程池
	 * @param maximumPoolSize          最大线程池
	 * @param keepAliveTime            线程存活时间
	 * @param unit                     单位
	 * @param rejectedExecutionHandler 拒绝策略
	 * @param threadFactory            线程工厂
	 * @param workQueue                队列
	 * @return DynamicThreadPoolExecutor
	 * @since 2021/8/20
	 */
	public DynamicThreadPoolExecutor createThreadPoolExecutor(String threadPoolName,
															  int corePoolSize,
															  int maximumPoolSize,
															  long keepAliveTime,
															  TimeUnit unit,
															  BlockingQueue<Runnable> workQueue,
															  ThreadFactory threadFactory,
															  RejectedExecutionHandler rejectedExecutionHandler) {
		Assert.notBlank(threadPoolName, "threadPoolName 不能为空");
		if (null == threadFactory) {
			threadFactory = new DynamicThreadFactory(threadPoolName);
		}
		if (null == rejectedExecutionHandler) {
			rejectedExecutionHandler = new DynamicAbortPolicy(threadPoolName);
		}

		DynamicThreadPoolExecutor poolExecutor = new DynamicThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, unit, workQueue, threadFactory, rejectedExecutionHandler);
		threadPoolExecutorMap.put(threadPoolName, poolExecutor);
		return poolExecutor;
	}

	/**
	 * 根据配置文件创建线程池
	 *
	 * @param dynamicThreadPoolProperties 配置文件
	 * @since 2021/8/20
	 */
	public void createThreadPoolExecutor(DynamicThreadPoolProperties dynamicThreadPoolProperties) {

		for (ThreadPoolProperties executor : dynamicThreadPoolProperties.getExecutors()) {
			if (!threadPoolExecutorMap.containsKey(executor.getThreadPoolName())) {
				DynamicThreadPoolExecutor threadPoolExecutor = new DynamicThreadPoolExecutor(executor.getCorePoolSize(),
						executor.getMaxPoolSize(), executor.getKeepAlive(), executor.getUnit(),
						getBlockingQueue(executor.getQueueType(), executor.getQueueCapacity(), executor.isFair()),
						new DynamicThreadFactory(executor.getThreadPoolName()),
						getRejectedExecutionHandler(executor.getRejectedExecutionType(), executor.getThreadPoolName()),
						executor.getThreadPoolName());

				threadPoolExecutorMap.put(executor.getThreadPoolName(), threadPoolExecutor);
			}
		}
	}

	/**
	 * 获取拒绝次数
	 *
	 * @param threadPoolName 线程池名称
	 * @return java.util.concurrent.atomic.AtomicLong
	 * @since 2022/3/3
	 */
	public AtomicLong getRejectCount(String threadPoolName) {
		return THREAD_POOL_EXECUTOR_REJECT_COUNT_MAP.get(threadPoolName);
	}

	/**
	 * 清空拒绝次数
	 *
	 * @param threadPoolName 线程名称
	 * @since 2022/3/3
	 */
	public void clearRejectCount(String threadPoolName) {
		THREAD_POOL_EXECUTOR_REJECT_COUNT_MAP.remove(threadPoolName);
	}


	/**
	 * 刷新线程池
	 *
	 * @param isWaitConfigRefreshOver 是否等待配置刷新结束
	 * @since 2022/3/3
	 */
	public void refreshThreadPoolExecutor(boolean isWaitConfigRefreshOver) {
		try {
			if (isWaitConfigRefreshOver) {
				// 等待Nacos配置刷新完成
				Thread.sleep(dynamicThreadPoolProperties.getWaitRefreshConfigSeconds() * 1000);
			}
		} catch (InterruptedException e) {

		}
		dynamicThreadPoolProperties.getExecutors().forEach(executor -> {
			ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(executor.getThreadPoolName());
			threadPoolExecutor.setCorePoolSize(executor.getCorePoolSize());
			threadPoolExecutor.setMaximumPoolSize(executor.getMaxPoolSize());
			threadPoolExecutor.setKeepAliveTime(executor.getKeepAlive(), executor.getUnit());
			threadPoolExecutor.setRejectedExecutionHandler(
					getRejectedExecutionHandler(executor.getRejectedExecutionType(), executor.getThreadPoolName()));
			BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();
			if (queue instanceof ResizableCapacityLinkedBlockIngQueue) {
				((ResizableCapacityLinkedBlockIngQueue<Runnable>) queue).setCapacity(executor.getQueueCapacity());
			}
		});
	}

	/**
	 * 监控全部的线程池
	 *
	 * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
	 * @since 2021/8/20
	 */
	public List<Map<String, Object>> monitor() {
		List<Map<String, Object>> result = new ArrayList<>();
		for (String s : threadPoolExecutorMap.keySet()) {

			result.add(monitor(s));
		}
		return result;
	}

	/**
	 * 监控线程池指标
	 *
	 * @param threadPoolName 线程池
	 * @return java.util.Map<java.lang.String, java.lang.Object>
	 * @since 2022/3/3
	 */
	public Map<String, Object> monitor(String threadPoolName) {
		DynamicThreadPoolExecutor poolExecutor = threadPoolExecutorMap.get(threadPoolName);
		if (null == poolExecutor) {
			return null;
		}
		ThreadPoolProperties threadPoolProperties = dynamicThreadPoolProperties.getExecutors().stream()
				.filter(a -> a.getThreadPoolName().endsWith(threadPoolName)).findFirst().orElse(null);

		AtomicLong rejectCount = this.getRejectCount(threadPoolName);

		Map<String, Object> pool = new HashMap<>(1);

		if (null != threadPoolProperties) {
			pool.putAll(BeanMap.create(threadPoolProperties));
		}
		pool.put("activeCount", poolExecutor.getActiveCount());
		pool.put("completedTaskCount", poolExecutor.getCompletedTaskCount());
		pool.put("largestPoolSize", poolExecutor.getLargestPoolSize());
		pool.put("taskCount", poolExecutor.getTaskCount());
		pool.put("rejectCount", rejectCount == null ? 0 : rejectCount.get());
		pool.put("waitTaskCount", poolExecutor.getQueue().size());
		return pool;
	}

	/**
	 * 获取拒绝策略
	 *
	 * @param rejectedExecutionType 拒绝类型
	 * @param threadPoolName        线程池名称
	 * @return RejectedExecutionHandler
	 */
	public static RejectedExecutionHandler getRejectedExecutionHandler(String rejectedExecutionType,
																	   String threadPoolName) {
		if (RejectedExecutionHandlerEnum.CALLER_RUNS_POLICY.getType().equals(rejectedExecutionType)) {
			return new ThreadPoolExecutor.CallerRunsPolicy();
		}
		if (RejectedExecutionHandlerEnum.DISCARD_OLDEST_POLICY.getType().equals(rejectedExecutionType)) {
			return new ThreadPoolExecutor.DiscardOldestPolicy();
		}
		if (RejectedExecutionHandlerEnum.DISCARD_POLICY.getType().equals(rejectedExecutionType)) {
			return new ThreadPoolExecutor.DiscardPolicy();
		}
		ServiceLoader<RejectedExecutionHandler> serviceLoader = ServiceLoader.load(RejectedExecutionHandler.class);
		Iterator<RejectedExecutionHandler> iterator = serviceLoader.iterator();
		while (iterator.hasNext()) {
			RejectedExecutionHandler rejectedExecutionHandler = iterator.next();
			String rejectedExecutionHandlerName = rejectedExecutionHandler.getClass().getSimpleName();
			if (rejectedExecutionType.equals(rejectedExecutionHandlerName)) {
				return rejectedExecutionHandler;
			}
		}
		return new DynamicAbortPolicy(threadPoolName);
	}

	/**
	 * 获取阻塞队列
	 *
	 * @param queueType     队列类型
	 * @param queueCapacity 队列大小
	 * @param fair          是否公平
	 * @return BlockingQueue
	 */
	public static BlockingQueue getBlockingQueue(String queueType, int queueCapacity, boolean fair) {
		if (!QueueTypeEnum.exists(queueType)) {
			throw new RuntimeException("队列不存在 " + queueType);
		}


		if (QueueTypeEnum.ARRAY_BLOCKING_QUEUE.getType().equals(queueType)) {
			return new ArrayBlockingQueue(queueCapacity);
		}
		if (QueueTypeEnum.SYNCHRONOUS_QUEUE.getType().equals(queueType)) {
			return new SynchronousQueue(fair);
		}
		if (QueueTypeEnum.PRIORITY_BLOCKING_QUEUE.getType().equals(queueType)) {
			return new PriorityBlockingQueue(queueCapacity);
		}
		if (QueueTypeEnum.DELAY_QUEUE.getType().equals(queueType)) {
			return new DelayQueue();
		}
		if (QueueTypeEnum.LINKED_BLOCKING_DEQUE.getType().equals(queueType)) {
			return new LinkedBlockingDeque(queueCapacity);
		}
		if (QueueTypeEnum.LINKED_TRANSFER_DEQUE.getType().equals(queueType)) {
			return new LinkedTransferQueue();
		}
		return new ResizableCapacityLinkedBlockIngQueue(queueCapacity);
	}

	/**
	 * 自定义线程工厂
	 *
	 * @author luyanan
	 * @since 2021/8/20
	 */
	static class DynamicThreadFactory implements ThreadFactory {

		private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);

		private  ThreadGroup group;

		private  AtomicInteger threadNumber = new AtomicInteger(1);

		private  String namePrefix;

		 DynamicThreadFactory(String threadPoolName) {
			SecurityManager securityManager = System.getSecurityManager();
			this.group = (securityManager != null) ? securityManager.getThreadGroup()
					: Thread.currentThread().getThreadGroup();
			this.namePrefix = threadPoolName + "-" + POOL_NUMBER.getAndIncrement() + "-thread-";
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(this.group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (thread.isDaemon()) {
				thread.setDaemon(false);
			}

			if (thread.getPriority() != Thread.NORM_PRIORITY) {
				thread.setPriority(Thread.NORM_PRIORITY);
			}
			return thread;
		}

	}

	/**
	 * 自定义拒绝策略
	 *
	 * @author luyanan
	 * @since 2021/8/20
	 */

	@NoArgsConstructor
	static class DynamicAbortPolicy implements RejectedExecutionHandler {

		private String threadPoolName;

		 DynamicAbortPolicy(String threadPoolName) {
			this.threadPoolName = threadPoolName;
		}

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

			AtomicLong atomicLong = THREAD_POOL_EXECUTOR_REJECT_COUNT_MAP.putIfAbsent(threadPoolName, new AtomicLong(1));
			if (atomicLong != null) {
				atomicLong.incrementAndGet();
			}
			throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + executor.toString());

		}

	}

}
