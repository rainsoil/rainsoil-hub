package com.rainsoil.common.framework.jdbc.annotation;


import com.rainsoil.common.framework.jdbc.FieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键id注解
 *
 * @author luyanan
 * @since 2021/3/8
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableId {

	/**
	 * id 的类型,默认自增
	 *
	 * @since 2021/3/8
	 */

	FieldType type() default FieldType.AUTO;

}
