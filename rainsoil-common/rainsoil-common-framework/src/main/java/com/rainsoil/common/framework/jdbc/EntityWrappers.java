package com.rainsoil.common.framework.jdbc;

import cn.hutool.core.collection.CollectionUtil;
import com.rainsoil.common.framework.jdbc.annotation.FieldStrategy;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * EntityWrapper工具类
 *
 * @param <T> 泛型
 * @author luyanan
 * @since 2021/3/9
 **/
@Data
public class EntityWrappers<T> {

	private T entity;

	/**
	 * 全局配置
	 *
	 * @since 2021/3/9
	 */

	private JdbcGlobalConfig jdbcGlobalConfig;

	/**
	 * 实体解析
	 *
	 * @since 2021/3/9
	 */

	private JdbcEntityParser<T> jdbcEntityParser;

	private EntityInfo<T> entityInfo;

	private EntityWrapper<T> entityWrapper;

	public EntityWrappers(T entity, JdbcGlobalConfig jdbcGlobalConfig, JdbcEntityParser<T> jdbcEntityParser) {
		this.entity = entity;
		this.jdbcGlobalConfig = jdbcGlobalConfig;
		this.jdbcEntityParser = jdbcEntityParser;
		this.entityInfo = jdbcEntityParser.getEntityInfo(this.entity);
		this.entityWrapper = new EntityWrapper<T>(entity);
	}

	public EntityWrappers(EntityWrapper<T> entityWrapper, JdbcGlobalConfig jdbcGlobalConfig,
						  JdbcEntityParser<T> jdbcEntityParser) {
		this.entityWrapper = entityWrapper;
		this.entity = entityWrapper.getEntity();
		this.jdbcGlobalConfig = jdbcGlobalConfig;
		this.jdbcEntityParser = jdbcEntityParser;
		this.entityInfo = jdbcEntityParser.getEntityInfo(this.entity);
	}

	/**
	 * 获取统计的sql
	 *
	 * @return com.rainsoil.common.framework.jdbc.EntityWrappers.SqlResult
	 * @since 2022/3/2
	 */
	public SqlResult getCountSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  COUNT(1) ");
		// 设置表名
		sql.append("  FROM ");
		sql.append(entityInfo.getTableName() + " ");
		setWhereSql(sql, jdbcGlobalConfig.getSelectStrategy(), false);
		return SqlResult.builder(sql.toString(), entityWrapper.getArgs(), this.entity.getClass(), this.entityInfo);
	}

	/**
	 * 获取查询SQL
	 *
	 * @return java.lang.String
	 * @since 2021/3/9
	 */
	public SqlResult getSelectSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		if (CollectionUtil.isEmpty(entityWrapper.getSelectSql())) {
			// 当没有设置查询条件的时候,默认设置为实体类所有的字段
			sql.append(entityInfo.getFieldInfoList().stream().map(EntityFieldInfo::getJdbcField)
					.collect(Collectors.joining(",")));
		} else {
			sql.append(entityWrapper.getSelectSql().stream().collect(Collectors.joining(",")));
		}
		// 设置表名
		sql.append("  FROM ");
		sql.append(entityInfo.getTableName() + " ");
		setWhereSql(sql, jdbcGlobalConfig.getSelectStrategy(), false);
		return SqlResult.builder(sql.toString(), entityWrapper.getArgs(), this.entity.getClass(), this.entityInfo);
	}

	/**
	 * 设置where 条件sql
	 *
	 * @param sql                 sql
	 * @param globalFieldStrategy 全局字段填充策略
	 * @param ignoreId            是否忽略id
	 * @since 2022/3/2
	 */
	public void setWhereSql(StringBuilder sql, FieldStrategy globalFieldStrategy, boolean ignoreId) {
		// 设置条件
		if (null != jdbcGlobalConfig && !globalFieldStrategy.equals(FieldStrategy.DEFAULT)) {
			Map<String, Object> fields = jdbcEntityParser.getFieldByStrategy(entityInfo.getFieldInfoList(),
					globalFieldStrategy, ignoreId);
			for (Map.Entry<String, Object> entry : fields.entrySet()) {
				entityWrapper.getWhereSql().add(SqlKeyword.AND.getSqlSegment(entry.getKey()));
				entityWrapper.getArgs().add(entry.getValue());
			}
		}
		if (CollectionUtil.isNotEmpty(entityWrapper.getWhereSql())) {
			sql.append(" WHERE 1 =1 ");
			for (String s : entityWrapper.getWhereSql()) {
				sql.append(s);
			}
		}
	}

	/**
	 * 获取插入的SQL
	 *
	 * @return java.lang.String
	 * @since 2021/3/9
	 */
	public SqlResult getInsertSql() {

		// 根据策略获取所有需要插入的字段
		Map<String, Object> fields = jdbcEntityParser.getFieldByStrategy(entityInfo.getFieldInfoList(),
				this.jdbcGlobalConfig.getInsertStrategy(), true);

		if (CollectionUtil.isEmpty(fields)) {
			throw new NullPointerException("没有需要插入的字段");
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT  INTO ").append(entityInfo.getTableName()).append("(")
				.append(fields.keySet().stream().collect(Collectors.joining(","))).append(")").append(" VALUES (")
				.append(fields.values().stream().map(a -> "?").collect(Collectors.joining(","))).append(")");
		fields.values().stream().forEach(a -> entityWrapper.getArgs().add(a));
		return SqlResult.builder(sql.toString(), entityWrapper.getArgs(), this.entity.getClass(), this.entityInfo);
	}

	/**
	 * 获取修改的SQL
	 *
	 * @return java.lang.String
	 * @since 2021/3/9
	 */
	public SqlResult getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE ");
		sql.append(this.entityInfo.getTableName());
		sql.append(" SET ");
		// 根据策略获取所有需要插入的字段
		Map<String, Object> fields = jdbcEntityParser.getFieldByStrategy(entityInfo.getFieldInfoList().stream()
				.filter(a -> !a.getFieldName().equals(this.entityInfo.getIdFieldInfo().getFieldName()))
				.collect(Collectors.toList()), this.jdbcGlobalConfig.getUpdateStrategy(), true);

		if (CollectionUtil.isEmpty(fields)) {
			throw new NullPointerException("没有需要插入的字段");
		}
		sql.append(fields.entrySet().stream().map(e -> {
			entityWrapper.getArgs().add(e.getValue());
			return e.getKey() + " = ?";
		}).collect(Collectors.joining(",")));
		return SqlResult.builder(sql.toString(), entityWrapper.getArgs(), this.entity.getClass(), this.entityInfo);
	}

	/**
	 * 删除的SQL
	 *
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.EntityWrappers.SqlResult
	 * @since 2021/3/14
	 */
	public SqlResult getDeleteSql() {

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(entityInfo.getTableName());
		setWhereSql(sql, this.jdbcGlobalConfig.getDeleteStrategy(), false);
		return SqlResult.builder(sql.toString(), entityWrapper.getArgs(), this.entity.getClass(), this.entityInfo);
	}


	/**
	 * sql结果
	 *
	 * @param <T> 泛型
	 * @author luyanan
	 * @since 2022/3/3
	 */
	@Data
	public static class SqlResult<T> {

		/**
		 * sql
		 *
		 * @since 2021/3/9
		 */

		private String sql;

		/**
		 * 参数
		 *
		 * @since 2021/3/9
		 */

		private List<Object> args = new ArrayList<>();

		/**
		 * 获取参数
		 *
		 * @return java.lang.Object[]
		 * @since 2022/3/3
		 */
		public Object[] args() {

			if (CollectionUtil.isEmpty(this.args)) {
				return new Object[]{};
			}
			return this.args.toArray();
		}

		/**
		 * class类型
		 *
		 * @since 2022/3/3
		 */

		private Class<? extends T> classType;

		/**
		 * 实体信息
		 *
		 * @since 2022/3/3
		 */

		private EntityInfo<T> entityInfo;

		/**
		 * 构建
		 *
		 * @param sql        sql
		 * @param args       参数
		 * @param classType  class类型
		 * @param entityInfo 实体信息
		 * @param <T>        泛型
		 * @return com.rainsoil.common.framework.jdbc.EntityWrappers.SqlResult<T>
		 * @since 2022/3/3
		 */
		public static <T> SqlResult<T> builder(String sql, List<Object> args, Class<?> classType,
											   EntityInfo<T> entityInfo) {

			SqlResult sqlResult = new SqlResult();
			sqlResult.setSql(sql);
			sqlResult.getArgs().addAll(args);
			sqlResult.setClassType(classType);
			// 清空args
			if (null != args) {
				args.clear();
			}
			sqlResult.setEntityInfo(entityInfo);
			return sqlResult;
		}

	}

}
