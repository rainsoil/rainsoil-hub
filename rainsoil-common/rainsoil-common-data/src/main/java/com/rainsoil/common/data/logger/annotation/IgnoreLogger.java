package com.rainsoil.common.data.logger.annotation;

/**
 * 忽略日志
 *
 * @author luyanan
 * @since 2021/8/22
 **/
public @interface IgnoreLogger {

	/**
	 * 默认忽略全部
	 *
	 * @since 2021/8/22
	 */

	Type type() default Type.ALL;

	/**
	 * 忽略类型
	 *
	 * @since 2021/8/22
	 */

	enum Type {

		/**
		 * 忽略请求参数
		 *
		 * @since 2021/1/22
		 */
		PARAMS,
		/**
		 * 忽略返回结果
		 *
		 * @author luyanan
		 * @since 2021/1/22
		 */
		RESULT,
		/**
		 * 全部忽略
		 *
		 * @author luyanan
		 * @since 2021/1/22
		 */
		ALL;

	}

}
