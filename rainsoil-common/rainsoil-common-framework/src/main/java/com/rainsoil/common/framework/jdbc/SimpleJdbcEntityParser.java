package com.rainsoil.common.framework.jdbc;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.core.constants.SymbolConstants;
import com.rainsoil.common.framework.jdbc.annotation.FieldStrategy;
import com.rainsoil.common.framework.jdbc.annotation.TableField;
import com.rainsoil.common.framework.jdbc.annotation.TableId;
import com.rainsoil.common.framework.jdbc.annotation.TableName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jdbc实体类解析
 *
 * @param <T> 泛型
 * @author luyanan
 * @since 2021/3/9
 **/
public class SimpleJdbcEntityParser<T> implements JdbcEntityParser<T> {

	/**
	 * 获取表名称
	 *
	 * @param entity 实体
	 * @return java.lang.String
	 * @since 2022/3/3
	 */
	private String getTableName(T entity) {
		Assert.notNull(entity, "实体类不能为空");

		if (entity.getClass().isAnnotationPresent(TableName.class)) {
			return entity.getClass().getAnnotation(TableName.class).value();
		}
		return nameToMysqlField(entity.getClass().getSimpleName());
	}

	/**
	 * 获取字段名
	 *
	 * @param field 字段
	 * @return java.lang.String
	 * @since 2022/3/3
	 */
	private String getSqlFieldName(Field field) {
		if (field.isAnnotationPresent(TableField.class)) {
			TableField tableField = field.getAnnotation(TableField.class);
			return tableField.value();
		} else {
			return nameToMysqlField(field.getName());
		}
	}

	@Override
	public Map<String, Object> getFieldByStrategy(List<EntityFieldInfo> entity, FieldStrategy fieldStrategy,
												  boolean ignoreId) {
		if (null == entity) {
			return null;
		}
		Map<String, Object> fields = new HashMap<>(16);

		for (EntityFieldInfo entityFieldInfo : entity) {
			if (ignoreId && null != entityFieldInfo.getFieldType()
					&& entityFieldInfo.getFieldType().equals(FieldType.AUTO)) {
				continue;
			}
			Object fieldValue = entityFieldInfo.getFieldValue();
			if (fieldStrategy.equals(FieldStrategy.DEFAULT)) {
				fields.put(entityFieldInfo.getJdbcField(), fieldValue);
			}
			if (fieldStrategy.equals(FieldStrategy.NOT_NULL)) {
				if (null != fieldValue) {
					fields.put(entityFieldInfo.getJdbcField(), fieldValue);
				}
			} else if (fieldStrategy.equals(FieldStrategy.NOT_EMPTY)) {
				if (fieldValue instanceof String) {
					String fieldValueStr = (String) fieldValue;
					if (StrUtil.isNotBlank(fieldValueStr)) {
						fields.put(entityFieldInfo.getJdbcField(), fieldValue);
					}
				} else {
					if (null != fieldValue) {
						fields.put(entityFieldInfo.getJdbcField(), fieldValue);
					}
				}
			}
		}
		return fields;
	}

	@Override
	public EntityInfo<T> getEntityInfo(T entity) {
		String tableName = this.getTableName(entity);
		EntityInfo entityInfo = new EntityInfo();
		entityInfo.setEntity(entity);
		entityInfo.setClazz(entity.getClass());
		entityInfo.setTableName(tableName);
		List<EntityFieldInfo> fieldInfoList = new ArrayList<>();
		EntityFieldInfo idFieldInfo = null;
		for (Field field : ReflectUtil.getFields(entity.getClass())) {
			if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			EntityFieldInfo entityFieldInfo = new EntityFieldInfo();
			entityFieldInfo.setFieldName(field.getName());
			entityFieldInfo.setJdbcField(getSqlFieldName(field));
			entityFieldInfo.setFieldValue(ReflectUtil.getFieldValue(entity, field));
			// 处理一下id字段
			if (field.isAnnotationPresent(TableId.class)) {
				TableId tableId = field.getAnnotation(TableId.class);
				entityFieldInfo.setFieldType(tableId.type());
				idFieldInfo = entityFieldInfo;
			}
			fieldInfoList.add(entityFieldInfo);
		}
		entityInfo.setIdFieldInfo(idFieldInfo);
		entityInfo.setFieldInfoList(fieldInfoList);
		return entityInfo;
	}

	/**
	 * 将驼峰名称转换为带下划线的
	 *
	 * @param fieldName 字段名
	 * @return java.lang.String
	 * @since 2021/3/9
	 */
	private String nameToMysqlField(String fieldName) {
		if (StrUtil.isBlank(fieldName)) {
			return fieldName;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fieldName.length(); i++) {
			char c = fieldName.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append("_" + Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		if (sb.toString().startsWith(SymbolConstants.UNDER_SCORE)) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

}
