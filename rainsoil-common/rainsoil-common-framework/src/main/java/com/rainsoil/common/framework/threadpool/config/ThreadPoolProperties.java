package com.rainsoil.common.framework.threadpool.config;

import com.rainsoil.common.framework.threadpool.enums.QueueTypeEnum;
import com.rainsoil.common.framework.threadpool.enums.RejectedExecutionHandlerEnum;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author luyanan
 * @since 2021/8/19
 **/
@Data
public class ThreadPoolProperties {

	/**
	 * 线程池名称
	 *
	 * @since 2021/8/19
	 */

	private String threadPoolName = "DynamicThreadPool";

	/**
	 * 核心线程池数量
	 *
	 * @since 2021/8/19
	 */

	private int corePoolSize = 1;

	/**
	 * 最大线程池数量
	 *
	 * @since 2021/8/19
	 */

	private int maxPoolSize = Integer.MAX_VALUE;

	/**
	 * 空闲线程存活时间
	 *
	 * @since 2021/8/19
	 */

	private int keepAlive = 60;

	/**
	 * 空闲线程存活时间单位
	 *
	 * @since 2021/8/19
	 */

	private TimeUnit unit = TimeUnit.SECONDS;

	/**
	 * 队列类型
	 *
	 * @since 2021/8/19
	 */

	private String queueType = QueueTypeEnum.LINKED_BLOCKING_QUEUE.getType();

	/**
	 * SynchronousQueue 是否公平策略
	 *
	 * @since 2021/8/19
	 */

	private boolean fair;

	/**
	 * 拒绝策略
	 *
	 * @since 2021/8/19
	 */

	private String rejectedExecutionType = RejectedExecutionHandlerEnum.ABORT_POLICY.getType();

	/**
	 * 队列最大数量
	 *
	 * @since 2021/8/19
	 */

	private int queueCapacity = Integer.MAX_VALUE;

	/**
	 * 告警阈值百分比
	 *
	 * @since 2021/8/23
	 */

	private int queueCapacityThreshold = 80;

}
