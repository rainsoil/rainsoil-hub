package com.rainsoil.common.framework.threadpool;

import com.rainsoil.common.framework.spring.SpringContextHolder;
import lombok.experimental.UtilityClass;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的静态方法
 *
 * @author luyanan
 * @since 2021/9/20
 **/
@UtilityClass
public class ThreadPools {

	/**
	 * 操作延迟10毫秒
	 */
	private static final int OPERATE_DELAY_TIME = 10;

	private static final String DEFAULT = "DEFAULT";

	/**
	 * 执行
	 *
	 * @param runnable 线程
	 * @since 2022/3/3
	 */
	public void execute(Runnable runnable) {
		DynamicThreadPoolManager dynamicThreadPoolManager = SpringContextHolder.getBean(DynamicThreadPoolManager.class);
		DynamicThreadPoolExecutor threadPoolExecutor = dynamicThreadPoolManager.createThreadPoolExecutor(DEFAULT);
		threadPoolExecutor.execute(runnable);
	}

	/**
	 * 延时线程任务
	 *
	 * @param task 任务
	 */
	public void scheduledExecute(TimerTask task) {
		ScheduledExecutorService executorService = SpringContextHolder.getBean(ScheduledExecutorService.class);
		executorService.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
	}

	/**
	 * 停止延时线程任务
	 */
	public void scheduledShutdown() {
		ScheduledExecutorService executorService = SpringContextHolder.getBean(ScheduledExecutorService.class);
		Threads.shutdownAndAwaitTermination(executorService);
	}

}
