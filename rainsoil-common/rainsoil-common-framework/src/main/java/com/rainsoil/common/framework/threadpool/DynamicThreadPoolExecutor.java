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

	private static ThreadLocal<Map<String, Object>> threadLocal = new TransmittableThreadLocal();


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

	public void execute(Runnable command, String taskName) {
		runnableNameMap.putIfAbsent(command.getClass().getSimpleName(), taskName);
		execute(command);
	}

	public Future<?> submit(Runnable task, String taskName) {
		runnableNameMap.putIfAbsent(task.getClass().getSimpleName(), taskName);
		return submit(task, null, defaultTaskName);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		if (null != threadInterceptors) {
			threadInterceptors.orderedStream().forEachOrdered(a -> {
				a.main(threadLocal);
			});
		}
		super.beforeExecute(t, r);
	}

	public <T> Future<T> submit(Callable<T> task, String taskName) {
		runnableNameMap.putIfAbsent(task.getClass().getSimpleName(), taskName);
		return super.submit(task);
	}

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

	protected Runnable doRunnable(Runnable runnable) {

		if (null == threadInterceptors) {
			return runnable;
		}
		return new AsyncRunnable(runnable, threadLocal, threadInterceptors);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		threadLocal.remove();
	}

}
