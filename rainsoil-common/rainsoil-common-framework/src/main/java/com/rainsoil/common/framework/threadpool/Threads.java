package com.rainsoil.common.framework.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 线程相关工具类.
 *
 * @author ruoyi
 */
@Slf4j
public class Threads {


	/**
	 * sleep等待,单位为毫秒
	 *
	 * @param milliseconds 毫秒数
	 */
	public static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			return;
		}
	}

	/**
	 * 等待时间
	 *
	 * @since 2022/2/8
	 */

	private static final int TIMEOUT = 120;

	/**
	 * 停止线程池 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务. 如果超时, 则调用shutdownNow,
	 * 取消在workQueue中Pending的任务,并中断所有阻塞函数. 如果仍人超時，則強制退出.
	 * 另对在shutdown时线程本身被调用中断做了处理.
	 *
	 * @param pool 执行器
	 */
	public static void shutdownAndAwaitTermination(ExecutorService pool) {
		if (pool != null && !pool.isShutdown()) {
			pool.shutdown();
			try {
				if (!pool.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
					pool.shutdownNow();
					if (!pool.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
						log.info("Pool did not terminate");
					}
				}
			} catch (InterruptedException ie) {
				pool.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * 打印线程异常信息
	 *
	 * @param r 线程
	 * @param t 异常
	 */
	public static void printException(Runnable r, Throwable t) {
		if (t == null && r instanceof Future<?>) {
			try {
				Future<?> future = (Future<?>) r;
				if (future.isDone()) {
					future.get();
				}
			} catch (CancellationException ce) {
				t = ce;
			} catch (ExecutionException ee) {
				t = ee.getCause();
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}
		if (t != null) {

			log.error(t.getMessage(), t);
		}
	}

}
