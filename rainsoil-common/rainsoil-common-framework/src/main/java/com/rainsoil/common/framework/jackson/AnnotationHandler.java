package com.rainsoil.common.framework.jackson;

import java.lang.annotation.Annotation;

/**
 * 注解处理器
 *
 * @author luyanan
 * @since 2021/10/17
 **/
public interface AnnotationHandler {

	/**
	 * 注解的类
	 * @return java.lang.Class<? extends java.lang.annotation.Annotation>
	 * @since 2021/10/17
	 */
	Class<? extends Annotation> annotationClass();

	/**
	 * 注解处理
	 * @param annotation 注解
	 * @param name 属性名
	 * @param value 属性值
	 * @return HandleResult 处理结果
	 * @since 2021/10/17
	 */
	HandleResult serialize(Annotation annotation, String name, Object value);

	/**
	 * deserialize
	 * @param annotation 注解
	 * @param name 字段名
	 * @param value 字段值
	 * @return com.rainsoil.fastjava.common.framework.jackson.HandleResult
	 * @since 2021/10/18
	 */
	HandleResult deserialize(Annotation annotation, String name, String value);

}
