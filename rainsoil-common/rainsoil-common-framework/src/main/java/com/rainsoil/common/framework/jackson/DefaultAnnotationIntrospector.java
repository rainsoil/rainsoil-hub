package com.rainsoil.common.framework.jackson;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luyanan
 * @since 2021/10/17
 **/
@Slf4j
public class DefaultAnnotationIntrospector extends JacksonAnnotationIntrospector {

	/**
	 * <自定义注解类名,注解处理类>
	 */
	private Map<String, AnnotationHandler> annotationHandlerMap;

	@Override
	public Object findDeserializer(Annotated am) {
		// 如果没有设置任何注解处理类，则直接返回
		if (annotationHandlerMap == null || annotationHandlerMap.isEmpty()) {
			return super.findSerializer(am);
		}
		Iterator<Map.Entry<String, AnnotationHandler>> it = annotationHandlerMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, AnnotationHandler> entry = it.next();
			// 获取注解
			Annotation an = getAnnotation(am, entry.getKey());
			if (an != null) {
				String fieldName = am.getName();
				// 获取属性名
				if (fieldName.startsWith("set")) {
					String first = String.valueOf(fieldName.charAt(3)).toLowerCase();
					fieldName = first + fieldName.substring(4);
				}
				if (log.isDebugEnabled()) {
					log.debug("serialize {} by {}", fieldName, entry.getValue());
				}
				return new AnnotationJsonDeserializer(fieldName, an, entry.getValue());
			}
		}
		return super.findSerializer(am);
	}

	@Override
	public Object findSerializer(Annotated am) {
		// 如果没有设置任何注解处理类，则直接返回
		if (annotationHandlerMap == null || annotationHandlerMap.isEmpty()) {
			return super.findSerializer(am);
		}
		Iterator<Map.Entry<String, AnnotationHandler>> it = annotationHandlerMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, AnnotationHandler> entry = it.next();
			// 获取注解
			Annotation an = getAnnotation(am, entry.getKey());
			if (an != null) {
				String fieldName = am.getName();
				// 获取属性名
				if (fieldName.startsWith("get")) {
					String first = String.valueOf(fieldName.charAt(3)).toLowerCase();
					fieldName = first + fieldName.substring(4);
				}
				if (log.isDebugEnabled()) {
					log.debug("serialize {} by {}", fieldName, entry.getValue());
				}
				return new AnnotationJsonSerializer(fieldName, an, entry.getValue());
			}
		}
		return super.findSerializer(am);
	}

	/**
	 * 获取注解
	 * @since 2022/2/8
	 * @param am  注解
	 * @param clsName  类名
	 * @return java.lang.annotation.Annotation
	 */
	private Annotation getAnnotation(Annotated am, String clsName) {
		try {
			Class<? extends Annotation> cls = (Class<? extends Annotation>) Class.forName(clsName);
			return am.getAnnotation(cls);
		}
		catch (Exception e) {
			log.error("getAnnotation error.", e);
		}
		return null;
	}

	@Override
	public Version version() {
		return super.version();
	}

	/**
	 *获取注解处理类
	 * @since 2022/2/8
	 * @return java.util.Map<java.lang.String, com.rainsoil.common.framework.jackson.AnnotationHandler>
	 */
	public Map<String, AnnotationHandler> getAnnotationHandlerMap() {
		return annotationHandlerMap;
	}

	/**
	 * 设置注解处理类
	 * @since 2022/2/8
	 * @param annotationHandlerMap 注解处理类map
	 */
	public void setAnnotationHandlerMap(Map<String, AnnotationHandler> annotationHandlerMap) {
		this.annotationHandlerMap = annotationHandlerMap;
	}

	/**
	 * 添加注解处理类
	 * @since 2022/2/8
	 * @param cls  注解class
	 * @param annotationHandler 注解处理类
	 */
	public void addAnnotationHandler(Class<? extends Annotation> cls, AnnotationHandler annotationHandler) {
		if (this.annotationHandlerMap == null) {
			this.annotationHandlerMap = new HashMap<>(5);
		}
		this.annotationHandlerMap.put(cls.getName(), annotationHandler);
	}

}
