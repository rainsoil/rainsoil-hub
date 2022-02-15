package com.rainsoil.common.file;


import com.rainsoil.common.file.core.annotation.FileHostIgnore;
import com.rainsoil.common.file.core.annotation.FileHostProperty;
import com.rainsoil.common.framework.jackson.AnnotationHandler;
import com.rainsoil.common.framework.jackson.HandleResult;
import com.rainsoil.common.framework.spring.SpringContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;

/**
 * 文件注解处理器
 *
 * @author luyanan
 * @since 2021/10/17
 **/
public class FileAnnotationHandler implements AnnotationHandler {

	@Autowired
	private FileTemplate fileTemplate;


	@Override
	public Class<? extends Annotation> annotationClass() {
		return FileHostProperty.class;
	}

	/**
	 * 注解处理
	 *
	 * @param annotation 注解
	 * @param name       属性名
	 * @param value      属性值
	 * @return HandleResult 处理结果
	 * @since 2021/10/17
	 */
	@Override
	public HandleResult serialize(Annotation annotation, String name, Object value) {
		Object newValue = value;
		HandlerMethod handlerMethod = SpringContextHolder.getHandlerMethod();
		FileHostIgnore fileHostIgnore = handlerMethod.getBean().getClass().getAnnotation(FileHostIgnore.class);
		if (null == fileHostIgnore) {
			fileHostIgnore = handlerMethod.getMethod().getAnnotation(FileHostIgnore.class);
		}
		if (isAddHost(fileHostIgnore, value)) {
			newValue = fileTemplate.addHost((String) value);
		}

		return new HandleResult(name, newValue, HandleResult.Type.REPLACE);
	}

	private static boolean isAddHost(FileHostIgnore fileHostIgnore, Object value) {
		return (null == fileHostIgnore || !fileHostIgnore.addHost()) && value instanceof String;
	}

	/**
	 * deserialize
	 *
	 * @param annotation
	 * @param name
	 * @param value
	 * @return com.rainsoil.fastjava.common.framework.jackson.HandleResult
	 * @since 2021/10/18
	 */
	@Override
	public HandleResult deserialize(Annotation annotation, String name, String value) {
		Object newValue = value;
		HandlerMethod handlerMethod = SpringContextHolder.getHandlerMethod();
		FileHostIgnore fileHostIgnore = handlerMethod.getBean().getClass().getAnnotation(FileHostIgnore.class);
		if (null == fileHostIgnore) {
			fileHostIgnore = handlerMethod.getMethod().getAnnotation(FileHostIgnore.class);
		}
		if (isRemoveHost(handlerMethod, fileHostIgnore, value)) {
			newValue = fileTemplate.removeHost(value);
		}
		return new HandleResult(name, newValue, HandleResult.Type.REPLACE);
	}

	/**
	 * 是否移除文件的host
	 *
	 * @param handlerMethod  handlerMethod
	 * @param fileHostIgnore 忽略注解
	 * @param value          属性值
	 * @return boolean
	 * @since 2022/2/6
	 */
	private static boolean isRemoveHost(HandlerMethod handlerMethod, FileHostIgnore fileHostIgnore, Object value) {

		return null != handlerMethod
				&& (null == fileHostIgnore
				|| !fileHostIgnore.removeHost())
				&& value instanceof String;
	}
}
