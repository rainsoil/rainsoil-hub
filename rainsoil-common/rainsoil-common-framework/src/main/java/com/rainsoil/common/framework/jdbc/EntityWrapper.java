package com.rainsoil.common.framework.jdbc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;

import com.rainsoil.common.core.lambda.LambdaUtils;
import com.rainsoil.common.core.lambda.Sfunction;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实体构建
 *
 * @param <T> 泛型
 * @author luyanan
 * @since 2021/3/8
 **/
@SuppressFBWarnings(value = "REFLC_REFLECTION_MAY_INCREASE_ACCESSIBILITY_OF_CLASS")
@Data
public class EntityWrapper<T> {

	private T entity;

	public EntityWrapper(T entity) {
		this.entity = entity;
	}

	@SneakyThrows
	public EntityWrapper(Class<? extends T> clazz) {
		this.entity = clazz.newInstance();
	}

	/**
	 * 条件SQL
	 *
	 * @since 2021/3/8
	 */

	private Set<String> whereSql = new HashSet();

	/**
	 * 查询SQL
	 *
	 * @since 2021/3/8
	 */

	private Set<String> selectSql = new HashSet<>();

	/**
	 * 最后追加的SQL
	 *
	 * @since 2021/3/8
	 */

	private String lastSql;

	/**
	 * 参数
	 *
	 * @author luyanan
	 * @since 2021/3/9
	 */
	private List<Object> args = new ArrayList<>();

	/**
	 * 添加查询的字段
	 *
	 * @param columns 字段
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.EntityWrapper<T>
	 * @since 2021/3/8
	 */
	public EntityWrapper<T> select(String... columns) {
		if (ArrayUtil.isEmpty(columns)) {
			return this;
		}
		selectSql.addAll(Arrays.stream(columns).collect(Collectors.toList()));
		return this;
	}

	/**
	 * 设置查询字段
	 *
	 * @param columns 字段
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.EntityWrapper<T>
	 * @since 2021/3/8
	 */
	public EntityWrapper<T> select(Sfunction<T, ?>... columns) {
		if (ArrayUtil.isEmpty(columns)) {
			return this;
		}
		LambdaUtils<T> lambdaUtils = new LambdaUtils<T>(this.entity);

		selectSql.addAll(Arrays.stream(columns).map(a -> lambdaUtils.columnToString(a)).collect(Collectors.toList()));
		return this;
	}

	/**
	 * 往SQL后面追加
	 *
	 * @param sql sql
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.EntityWrapper<T>
	 * @since 2021/3/8
	 */
	public EntityWrapper<T> lastSql(String sql) {
		this.lastSql = sql;
		return this;
	}

	/**
	 * like
	 *
	 * @param condition 触发条件
	 * @param column    字段
	 * @param val       值
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.EntityWrapper<T>
	 * @since 2021/3/8
	 */
	public EntityWrapper<T> like(boolean condition, String column, Object val) {
		return like(condition, column, val, SqlLike.DEFAULT);
	}

	/**
	 * like拼接
	 *
	 * @param condition 触发条件
	 * @param column    字段
	 * @param val       值
	 * @param sqlLike   类型
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.EntityWrapper<T>
	 * @since 2021/3/8
	 */
	public EntityWrapper<T> like(boolean condition, String column, Object val, SqlLike sqlLike) {
		if (!condition) {
			return this;
		}
		appendLike(column, val, sqlLike);
		return this;
	}

	/**
	 * 右like拼接
	 *
	 * @param condition 触发条件
	 * @param column    字段
	 * @param val       值
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.EntityWrapper<T>
	 * @since 2021/3/8
	 */
	public EntityWrapper<T> rightLike(boolean condition, String column, Object val) {
		return like(condition, column, val, SqlLike.RIGHT);
	}

	/**
	 * 左like拼接
	 *
	 * @param condition 触发条件
	 * @param column    字段
	 * @param val       值
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.EntityWrapper<T>
	 * @since 2021/3/8
	 */
	public EntityWrapper<T> leftLike(boolean condition, String column, Object val) {
		return like(condition, column, val, SqlLike.LEFT);
	}

	/**
	 * in
	 *
	 * @param condition 触发tiaojian
	 * @param column    字段
	 * @param vals      值
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.EntityWrapper<T>
	 * @since 2021/3/9
	 */
	public EntityWrapper<T> in(boolean condition, String column, Collection<? extends Serializable> vals) {
		if (CollectionUtil.isEmpty(vals) || !condition) {
			return this;
		}
		whereSql.add(SqlKeyword.IN.getSqlSegment(column) + "("
				+ vals.stream().map(a -> a.toString()).collect(Collectors.joining(",")) + ")");
		return this;
	}

	/**
	 * 等于
	 *
	 * @param condition 触发条件
	 * @param column    字段
	 * @param val       值
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.EntityWrapper<T>
	 * @since 2021/3/9
	 */
	public EntityWrapper<T> eq(boolean condition, String column, Object val) {
		if (!condition) {
			return this;
		}

		whereSql.add(SqlKeyword.AND.getSqlSegment(column));
		args.add(val);
		return this;
	}
	//

	/**
	 * 添加Like 语句
	 *
	 * @param column  字段
	 * @param val     值
	 * @param sqlLike like类型
	 * @since 2021/3/8
	 */
	private void appendLike(String column, Object val, SqlLike sqlLike) {
		String right = "";
		String left = "";
		if (sqlLike.equals(SqlLike.DEFAULT)) {
			right = "%";
			left = "%";
		} else if (sqlLike.equals(SqlLike.LEFT)) {
			left = "%";
		} else if (sqlLike.equals(SqlLike.RIGHT)) {
			right = "%";
		}
		String sql = SqlKeyword.LIKE.getSqlSegment(column, left, right);
		args.add(val);
		whereSql.add(sql);
	}

}
