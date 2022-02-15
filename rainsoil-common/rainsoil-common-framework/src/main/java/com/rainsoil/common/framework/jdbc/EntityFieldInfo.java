package com.rainsoil.common.framework.jdbc;

import lombok.Data;

/**
 * 实体字段详情
 *
 * @author luyanan
 * @since 2021/3/10
 **/
@Data
public class EntityFieldInfo {

	/**
	 * 字段名称
	 *
	 * @since 2021/3/10
	 */

	private String fieldName;

	/**
	 * 数据库的字段
	 *
	 * @since 2021/3/10
	 */

	private String jdbcField;

	/**
	 * 字段的值
	 *
	 * @since 2021/3/10
	 */

	private Object fieldValue;

	/**
	 * 字段类型
	 *
	 * @since 2021/3/10
	 */

	private FieldType fieldType;

}
