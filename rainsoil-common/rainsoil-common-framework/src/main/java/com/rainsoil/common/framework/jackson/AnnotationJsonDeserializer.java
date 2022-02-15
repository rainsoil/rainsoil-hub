package com.rainsoil.common.framework.jackson;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 注解json反序列化
 *
 * @author luyanan
 * @since 2021/10/17
 **/
public class AnnotationJsonDeserializer extends JsonDeserializer<Object> {

	/**
	 * 注解
	 */
	private final Annotation annotation;

	/**
	 * 属性名
	 */
	private final String name;

	/**
	 * 注解处理类
	 */
	private final AnnotationHandler handler;

	public AnnotationJsonDeserializer(String name, Annotation annotation, AnnotationHandler handler) {
		this.name = name;
		this.annotation = annotation;
		this.handler = handler;
	}

	@Override
	public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {

		String text = jsonParser.getText();
		if (null == handler) {
			return text;
		}
		HandleResult handleResult = handler.deserialize(this.annotation, this.name, text);

		if (null == handleResult || null == handleResult.getValue()) {
			return text;
		}
		return handleResult.getValue();
	}

}
