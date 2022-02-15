package com.rainsoil.common.framework.jackson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * @author luyanan
 * @since 2021/10/17
 **/
@Slf4j
public class AnnotationJsonSerializer extends JsonSerializer<Object> {


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

	public AnnotationJsonSerializer(String name, Annotation annotation, AnnotationHandler handler) {
		this.name = name;
		this.annotation = annotation;
		this.handler = handler;
	}

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if (handler == null) {
			gen.writeObject(value);
			return;
		}

		HandleResult result = handler.serialize(this.annotation, this.name, value);
		if (result == null) {
			gen.writeObject(value);
			return;
		}

		if (result.getValue() == null && result.getFieldName() == null) {
			gen.writeObject(value);
		}
		else {
			// 创建新的属性
			if (HandleResult.Type.NEW == result.getType()) {
				gen.writeObject(value);
				if (result.getValue() != null) {
					gen.writeObjectField(result.getFieldName(), result.getValue());
				}
			}
			else {
				// 覆盖原有属性值
				gen.writeObject(result.getValue());
			}
		}
		/**
		 * 如果有其他属性，则继续序列化
		 */
		if (result.getFields() != null && !result.getFields().isEmpty()) {
			Iterator<Map.Entry<String, Object>> it = result.getFields().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				gen.writeObjectField(formatFieldName(entry.getKey()), entry.getValue());
			}
		}
	}

	/**
	 * 格式化字段名称
	 * @since 2022/2/8
	 * @param fieldName 字段名称
	 * @return java.lang.String
	 */
	private String formatFieldName(String fieldName) {
		int i = fieldName.lastIndexOf('.');
		if (i < 0) {
			return fieldName;
		}
		return fieldName.substring(i + 1);
	}

}
