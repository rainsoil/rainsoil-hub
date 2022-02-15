package com.rainsoil.common.framework.threadpool.alarm;


import com.rainsoil.common.framework.spring.SpringContextHolder;
import com.rainsoil.common.framework.threadpool.DynamicThreadPoolExecutor;
import com.rainsoil.common.framework.threadpool.DynamicThreadPoolManager;
import com.rainsoil.common.framework.threadpool.config.DynamicThreadPoolProperties;
import com.rainsoil.common.framework.threadpool.config.ThreadPoolProperties;
import com.rainsoil.common.framework.threadpool.event.DynamicThreadPoolAlarmEvent;
import com.rainsoil.common.framework.threadpool.event.ThreadPoolAlarmMessageVo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程池告警
 *
 * @author luyanan
 * @since 2021/8/23
 **/
public class DynamicThreadPoolAlarm {

	@Autowired
	private DynamicThreadPoolManager dynamicThreadPoolManager;

	@Autowired
	private DynamicThreadPoolProperties dynamicThreadPoolProperties;

	/**
	 * 初始化方法
	 *
	 * @since 2022/2/8
	 */
	@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
	@PostConstruct
	public void init() {

		new Thread(() -> {
			while (true) {

				for (Map.Entry<String, DynamicThreadPoolExecutor> entry : dynamicThreadPoolManager
						.getThreadPoolExecutorMap().entrySet()) {
					String threadPoolName = entry.getKey();
					DynamicThreadPoolExecutor poolExecutor = entry.getValue();
					// 获取配置
					ThreadPoolProperties threadPoolProperties = dynamicThreadPoolProperties.getExecutors().stream()
							.filter(a -> a.getThreadPoolName().endsWith(threadPoolName)).findFirst().orElse(null);
					// 告警阈值,默认为线程的
					int queueCapacityThreshold = dynamicThreadPoolProperties.getQueueCapacityThreshold();
					if (null != threadPoolProperties) {
						queueCapacityThreshold = threadPoolProperties.getQueueCapacityThreshold();
					}
					if (queueCapacityThreshold > 100) {
						throw new IllegalStateException("queueCapacityThreshold 不能大于100");
					}

					if (poolExecutor.getQueue().size() > (queueCapacityThreshold * poolExecutor.getMaximumPoolSize())) {
						// 告警
						SpringContextHolder.publishEvent(new DynamicThreadPoolAlarmEvent(
								ThreadPoolAlarmMessageVo.builder().taskCount(poolExecutor.getQueue().size())
										.threadPoolName(threadPoolName).queueCapacityThreshold(queueCapacityThreshold)
										.maximumPoolSize(poolExecutor.getMaximumPoolSize())
										.type(ThreadPoolAlarmMessageVo.TYPE.QUEUE_CAPACITY_THRESHOLD).build()));
					}
					AtomicLong rejectCount = dynamicThreadPoolManager.getRejectCount(threadPoolName);
					if (rejectCount != null && rejectCount.get() > 0) {
						// 告警
						SpringContextHolder.publishEvent(new DynamicThreadPoolAlarmEvent(ThreadPoolAlarmMessageVo
								.builder().taskCount(poolExecutor.getQueue().size()).threadPoolName(threadPoolName)
								.queueCapacityThreshold(queueCapacityThreshold)
								.maximumPoolSize(poolExecutor.getMaximumPoolSize()).rejectCount(rejectCount.get())
								.type(ThreadPoolAlarmMessageVo.TYPE.REJECTCOUNT).build()));
						// 清空拒绝数据
						dynamicThreadPoolManager.clearRejectCount(threadPoolName);
					}

				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
