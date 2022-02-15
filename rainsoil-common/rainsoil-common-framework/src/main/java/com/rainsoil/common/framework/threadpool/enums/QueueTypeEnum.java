package com.rainsoil.common.framework.threadpool.enums;

import lombok.Getter;

/**
 * 队列类型
 *
 * @author luyanan
 * @since 2021/8/19
 **/
@Getter
public enum QueueTypeEnum {

	/**
	 * LinkedBlockingQueue
	 *
	 * @since 2021/8/25
	 */

	LINKED_BLOCKING_QUEUE("LinkedBlockingQueue"),
	/**
	 * SynchronousQueue
	 *
	 * @since 2021/8/25
	 */

	SYNCHRONOUS_QUEUE("SynchronousQueue"),
	/**
	 * ArrayBlockingQueue
	 *
	 * @since 2021/8/25
	 */

	ARRAY_BLOCKING_QUEUE("ArrayBlockingQueue"),
	/**
	 * DelayQueue
	 *
	 * @since 2021/8/25
	 */

	DELAY_QUEUE("DelayQueue"),
	/**
	 * LinkedTransferQueue
	 *
	 * @since 2021/8/25
	 */

	LINKED_TRANSFER_DEQUE("LinkedTransferQueue"),
	/**
	 * LinkedBlockingDeque
	 *
	 * @since 2021/8/25
	 */

	LINKED_BLOCKING_DEQUE("LinkedBlockingDeque"),
	/**
	 * PriorityBlockingQueue
	 *
	 * @since 2021/8/25
	 */

	PRIORITY_BLOCKING_QUEUE("PriorityBlockingQueue");

	QueueTypeEnum(String type) {
		this.type = type;
	}

	private String type;

	/**
	 * 类型是否存在
	 * @param type 类型
	 * @return boolean
	 * @since 2021/8/19
	 */
	public static boolean exists(String type) {
		for (QueueTypeEnum typeEnum : QueueTypeEnum.values()) {
			if (typeEnum.getType().equals(type)) {
				return true;
			}
		}
		return false;
	}

}
