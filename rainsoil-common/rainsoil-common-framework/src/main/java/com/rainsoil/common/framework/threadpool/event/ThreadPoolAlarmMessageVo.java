package com.rainsoil.common.framework.threadpool.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 告警信息
 *
 * @author luyanan
 * @since 2021/8/24
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ThreadPoolAlarmMessageVo {

	/**
	 * 类型
	 *
	 * @since 2021/8/24
	 */
	private TYPE type;

	/**
	 * 设置的报警阈值
	 *
	 * @since 2021/8/24
	 */

	private int queueCapacityThreshold;

	/**
	 * 允许的最大大小
	 *
	 * @since 2021/8/24
	 */

	private int maximumPoolSize;

	/**
	 * 任务数量
	 *
	 * @since 2021/8/24
	 */

	private int taskCount;

	/**
	 * 线程池名称
	 *
	 * @since 2021/8/24
	 */

	private String threadPoolName;

	/**
	 * 拒绝次数
	 *
	 * @since 2021/8/24
	 */

	private Long rejectCount;

	public enum TYPE {

		/**
		 * 队列容量超过阈值
		 *
		 * @since 2021/8/24
		 */

		QUEUE_CAPACITY_THRESHOLD("QUEUE_CAPACITY_THRESHOLD"),

		/**
		 * 拒绝次数超过阈值
		 *
		 * @since 2021/8/24
		 */

		REJECTCOUNT("REJECTCOUNT");
		;

		public String getType() {
			return type;
		}

		TYPE(String type) {
			this.type = type;
		}

		private String type;

	}

}
