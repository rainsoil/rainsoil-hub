package com.rainsoil.common.framework.threadpool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 拒绝策略枚举类
 *
 * @author luyanan
 * @since 2021/8/19
 **/
@AllArgsConstructor
@Getter
public enum RejectedExecutionHandlerEnum {

	/**
	 * CallerRunsPolicy
	 *
	 * @since 2021/8/25
	 */

	CALLER_RUNS_POLICY("CallerRunsPolicy"),
	/**
	 * AbortPolicy
	 *
	 * @since 2021/8/25
	 */

	ABORT_POLICY("AbortPolicy"),
	/**
	 * DiscardPolicy
	 *
	 * @since 2021/8/25
	 */

	DISCARD_POLICY("DiscardPolicy"),
	/**
	 * DiscardOldestPolicy
	 *
	 * @since 2021/8/25
	 */

	DISCARD_OLDEST_POLICY("DiscardOldestPolicy");

	private String type;

	public static boolean exists(String type) {
		for (RejectedExecutionHandlerEnum typeEnum : RejectedExecutionHandlerEnum.values()) {
			if (typeEnum.getType().equals(type)) {
				return true;
			}
		}
		return false;
	}

}
