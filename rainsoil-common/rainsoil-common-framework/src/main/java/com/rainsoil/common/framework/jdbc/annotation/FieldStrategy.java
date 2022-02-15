package com.rainsoil.common.framework.jdbc.annotation;

/**
 * 字段策略枚举类
 *
 * @author luyanan
 * @since 2021/3/9
 **/
public enum FieldStrategy {

	/**
	 * 非NULL判断
	 */
	NOT_NULL,
	/**
	 * 非空判断(只对字符串类型字段,其他类型字段依然为非NULL判断)
	 */
	NOT_EMPTY,
	/**
	 * 默认的,一般只用于注解里
	 * <p>
	 * 1. 在全局里代表 NOT_NULL
	 * </p>
	 * <p>
	 * 2. 在注解里代表 跟随全局
	 * </p>
	 */
	DEFAULT

}
