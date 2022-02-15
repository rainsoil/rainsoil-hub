package com.rainsoil.common.framework.jdbc;

import lombok.Data;

import java.util.List;

/**
 * 实体详情
 *
 * @author luyanan
 * @since 2021/3/10
 **/
@Data
public class EntityInfo<T> {

	/**
	 * chass
	 *
	 * @since 2021/3/10
	 */

	private Class<T> clazz;

	/**
	 * 实体
	 *
	 * @since 2021/3/10
	 */

	private T entity;

	/**
	 * 表名称
	 *
	 * @since 2021/3/10
	 */

	private String tableName;

	/**
	 * 字段的值
	 *
	 * @since 2021/3/10
	 */

	private List<EntityFieldInfo> fieldInfoList;

	/**
	 * id的字段
	 *
	 * @since 2021/3/10
	 */

	private EntityFieldInfo idFieldInfo;

}
