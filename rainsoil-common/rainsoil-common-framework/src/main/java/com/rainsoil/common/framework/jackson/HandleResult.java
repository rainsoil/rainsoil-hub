package com.rainsoil.common.framework.jackson;

import java.util.HashMap;
import java.util.Map;

/**
 * 结果处理
 *
 * @author luyanan
 * @since 2021/10/17
 **/
public class HandleResult {

	public enum Type {

		/**
		 * 创建新的序列化属性
		 */
		NEW,
		/**
		 * 覆盖原有属性值
		 */
		REPLACE;

	}

	/**
	 * 序列化属性名称
	 */
	private final String fieldName;

	/**
	 * 序列化属性值
	 */
	private final Object value;

	/**
	 * 序列化赋值方式
	 */
	private final Type type;

	/**
	 * 其他序列化属性集合，便于扩展适用于特殊场景
	 */
	private Map<String, Object> fields;

	public HandleResult(String fieldName, Object value, Type type) {
		this.fieldName = fieldName;
		this.type = type;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}

	/**
	 * 添加字段
	 * @since 2022/2/8
	 * @param name 字段名
	 * @param value  字段值
	 * @return com.rainsoil.common.framework.jackson.HandleResult
	 */
	public HandleResult addField(String name, Object value) {
		if (this.fields == null) {
			this.fields = new HashMap<>(8);
		}
		this.fields.put(name, value);
		return this;
	}

	public Map<String, Object> getFields() {
		return fields;
	}

}
