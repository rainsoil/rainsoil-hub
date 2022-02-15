package com.rainsoil.common.file.core.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

/**
 * 文件host字段注解
 *
 * @author luyanan
 * @since 2021/10/14
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@JacksonAnnotationsInside
public @interface FileHostProperty {

	/**
	 * 添加host
	 * @return boolean
	 * @since 2021/10/14
	 */
	boolean addHost() default true;

	/**
	 * 移除host
	 * @return boolean
	 * @since 2021/10/14
	 */
	boolean removeHost() default true;

}
