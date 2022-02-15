package com.rainsoil.common.framework.threadpool.scheduled;

import com.rainsoil.common.framework.threadpool.Threads;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * ScheduledExecutorService 自动配置
 *
 * @author luyanan
 * @since 2021/9/20
 **/
@Configuration
public class ScheduledExecutorServiceAutoConfiguration {

	/**
	 * 执行周期性或定时任务
	 */
	@Bean(name = "scheduledExecutorService")
	protected ScheduledExecutorService scheduledExecutorService() {
		return new ScheduledThreadPoolExecutor(50,
				new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build()) {
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				Threads.printException(r, t);
			}
		};
	}

}
