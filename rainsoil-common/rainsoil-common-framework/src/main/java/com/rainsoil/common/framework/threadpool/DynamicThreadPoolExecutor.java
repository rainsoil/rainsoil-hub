package com.rainsoil.common.framework.threadpool;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.rainsoil.common.framework.threadpool.config.ThreadPoolProperties;
import com.rainsoil.common.framework.threadpool.interceptor.DynamicThreadInterceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 自定义动态ThreadPoolExecutor
 *
 * @author luyanan
 * @since 2021/8/20
 **/
public class DynamicThreadPoolExecutor extends ThreadPoolExecutor {

	@Autowired(required = false)
	private ObjectProvider<DynamicThreadInterceptor> threadInterceptors;

	private static ThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal();


	private String defaultTaskName = "defaultTask";

	/**
	 * The default rejected execution handler
	 */
	private static final RejectedExecutionHandler DEFAULT_HANDLER = new AbortPolicy();

	private Map<String, String> runnableNameMap = new ConcurrentHashMap<>();

	public DynamicThreadPoolExecutor(ThreadPoolProperties threadPoolProperties) {
		super(threadPoolProperties.getCorePoolSize(), threadPoolProperties.getMaxPoolSize(),
				threadPoolProperties.getKeepAlive(), threadPoolProperties.getUnit(),
				DynamicThreadPoolManager.getBlockingQueue(threadPoolProperties.getQueueType(),
						threadPoolProperties.getQueueCapacity(), threadPoolProperties.isFair()));

	}

	public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
									 BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
									 BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, DEFAULT_HANDLER);
	}

	public DynamicThreadPoolExecutor(int corePoolSize,
									 int maximumPoolSize,
									 long keepAliveTime, TimeUnit unit,
									 BlockingQueue<Runnable> workQueue,
									 ThreadFactory threadFactory,
									 RejectedExecutionHandler handler,
									 String threadPoolName) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		this.defaultTaskName = threadPoolName;
	}

	public DynamicThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
									 BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
									 RejectedExecutionHandler rejectedExecutionHandler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, rejectedExecutionHandler);
	}

	@Override
	public void execute(Runnable command) {
		runnableNameMap.putIfAbsent(command.getClass().getSimpleName(), defaultTaskName);
		super.execute(doRunnable(command));
	}

	/**
	 * 执行
	 *
	 * @param command  线程
	 * @param taskName 线程任务
	 * @since 2022/3/3
	 */
	public void execute(Runnable command, String taskName) {
		runnableNameMap.putIfAbsent(command.getClass().getSimpleName(), taskName);
		execute(command);
	}

	/**
	 * 提交任务
	 *
	 * @param task     task
	 * @param taskName 任务名称
	 * @return java.util.concurrent.Future<?>
	 * @since 2022/3/3
	 */
	public Future<?> submit(Runnable task, String taskName) {
		runnableNameMap.putIfAbsent(task.getClass().getSimpleName(), taskName);
		return submit(task, null, defaultTaskName);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		if (null != threadInterceptors) {
			threadInterceptors.orderedStream().forEachOrdered(a -> {
				a.main(THREAD_LOCAL);
			});
		}
		super.beforeExecute(t, r);
	}

	/**
	 * 提交任务
	 *
	 * @param task     任务
	 * @param taskName 任务名称
	 * @param <T>      泛型
	 * @return java.util.concurrent.Future<T>
	 * @since 2022/3/3
	 */
	public <T> Future<T> submit(Callable<T> task, String taskName) {
		runnableNameMap.putIfAbsent(task.getClass().getSimpleName(), taskName);
		return super.submit(task);
	}

	/**
	 * 提交任务
	 *
	 * @param task     任务
	 * @param result   结果
	 * @param taskName 任务名称
	 * @param <T>      泛型
	 * @return java.util.concurrent.Future<T>
	 * @since 2022/3/3
	 */
	public <T> Future<T> submit(Runnable task, T result, String taskName) {
		runnableNameMap.putIfAbsent(task.getClass().getSimpleName(), taskName);
		return super.submit(doRunnable(task), result);
	}

	@Override
	public Future<?> submit(Runnable task) {
		runnableNameMap.putIfAbsent(task.getClass().getSimpleName(), defaultTaskName);
		return super.submit(task);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		runnableNameMap.putIfAbsent(task.getClass().getSimpleName(), defaultTaskName);
		return super.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		runnableNameMap.putIfAbsent(task.getClass().getSimpleName(), defaultTaskName);
		return super.submit(task, result);
	}

	/**
	 * 执行线程任务
	 *
	 * @param runnable 线程
	 * @return java.lang.Runnable
	 * @since 2022/3/3
	 */
	protected Runnable doRunnable(Runnable runnable) {

		if (null == threadInterceptors) {
			return runnable;
		}
		return new AsyncRunnable(runnable, THREAD_LOCAL, threadInterceptors);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		THREAD_LOCAL.remove();
	}

}
