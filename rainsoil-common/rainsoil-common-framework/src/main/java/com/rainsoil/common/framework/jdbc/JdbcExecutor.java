package com.rainsoil.common.framework.jdbc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;

import com.rainsoil.common.core.page.OrderItem;
import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * jdbc执行器
 *
 * @author luyanan
 * @since 2021/3/8
 **/
public class JdbcExecutor extends JdbcTemplate {

	private JdbcGlobalConfig jdbcGlobalConfig = new JdbcGlobalConfig();

	private JdbcEntityParser jdbcEntityParser = new SimpleJdbcEntityParser();

	/**
	 * 设置全局配置文件
	 *
	 * @param jdbcGlobalConfig 全局配置文件
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.jdbc.JdbcExecutor
	 * @since 2021/3/9
	 */
	public JdbcExecutor jdbcGlobalConfig(JdbcGlobalConfig jdbcGlobalConfig) {
		this.jdbcGlobalConfig = jdbcGlobalConfig;
		return this;
	}

	public JdbcExecutor jdbcEntityParser(JdbcEntityParser jdbcEntityParser) {
		this.jdbcEntityParser = jdbcEntityParser;
		return this;
	}

	public EntityWrappers getEntityWrappers(Object entity) {
		return new EntityWrappers<>(entity, this.jdbcGlobalConfig, this.jdbcEntityParser);
	}

	@SneakyThrows
	public <T> EntityWrappers getEntityWrappers(Class<T> clazz) {
		return new EntityWrappers<T>(clazz.newInstance(), this.jdbcGlobalConfig,
				(JdbcEntityParser<T>) this.jdbcEntityParser);
	}

	private <T> EntityWrappers<T> getEntityWrappers(EntityWrapper<T> entityWrapper) {
		return new EntityWrappers<T>(entityWrapper, this.jdbcGlobalConfig, (JdbcEntityParser<T>) this.jdbcEntityParser);
	}

	/**
	 * 根据id查询
	 *
	 * @param id           id
	 * @param requiredType
	 * @return T
	 * @since 2021/3/9
	 */
	public <T> T selectById(Serializable id, Class<T> requiredType) {
		StringBuilder sql = new StringBuilder();
		EntityWrappers entityWrappers = getEntityWrappers(requiredType);
		EntityWrappers.SqlResult selectSql = entityWrappers.getSelectSql();
		sql.append(selectSql.getSql());
		sql.append(" WHERE " + SqlKeyword.EQ.getSqlSegment(selectSql.getEntityInfo().getIdFieldInfo().getJdbcField()));
		List<T> ts = this.query(sql.toString(), new BeanPropertyRowMapper<T>(requiredType), id);
		return CollectionUtil.isEmpty(ts) ? null : ts.get(0);
	}

	/**
	 * 查询单条数据
	 *
	 * @param entityWrapper
	 * @return T
	 * @since 2021/3/9
	 */
	public <T> T selectOne(EntityWrapper<T> entityWrapper, Class<T> requiredType) {
		EntityWrappers.SqlResult sqlResult = getEntityWrappers(entityWrapper).getSelectSql();
		List<T> list = this.query(sqlResult.getSql(), new BeanPropertyRowMapper<T>(requiredType), sqlResult.args());
		return CollectionUtil.isEmpty(list) ? null : list.get(0);
	}

	/**
	 * 查询列表
	 *
	 * @param entityWrapper
	 * @param requiredType
	 * @return java.util.List<T>
	 * @since 2021/3/9
	 */
	public <T> List<T> selectList(EntityWrapper<T> entityWrapper, Class<T> requiredType) {
		EntityWrappers.SqlResult sqlResult = getEntityWrappers(entityWrapper).getSelectSql();
		List<T> list = this.query(sqlResult.getSql(), new BeanPropertyRowMapper<T>(requiredType), sqlResult.args());
		return list;
	}

	/**
	 * 根据id集合批量查询
	 *
	 * @param idList
	 * @param requiredType
	 * @return java.util.List<T>
	 * @since 2021/3/9
	 */
	public <T> List<T> selectBatchIds(Collection<? extends Serializable> idList, Class<T> requiredType) {
		EntityWrapper<T> entityWrapper = new EntityWrapper<>(requiredType);
		entityWrapper.in(false, getEntityWrappers(requiredType).getEntityInfo().getIdFieldInfo().getFieldName(),
				idList);
		EntityWrappers.SqlResult sqlResult = getEntityWrappers(entityWrapper).getSelectSql();
		return this.query(sqlResult.getSql(), new BeanPropertyRowMapper<T>(requiredType), sqlResult.args());
	}

	/**
	 * 根据条件查询
	 *
	 * @param wrapper
	 * @return java.lang.Integer
	 * @since 2021/3/9
	 */
	public <T> Integer selectCount(EntityWrapper<T> wrapper) {
		EntityWrappers<T> entityWrappers = getEntityWrappers(wrapper);
		EntityWrappers.SqlResult countSql = entityWrappers.getCountSql();
		return this.queryForObject(countSql.getSql(), Integer.class, countSql.args());
	}

	/**
	 * 插入
	 *
	 * @param entity
	 * @return int
	 * @since 2021/3/9
	 */
	public <T> int insert(T entity) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		EntityWrappers wrappers = getEntityWrappers(entity);
		EntityWrappers.SqlResult insertSql = wrappers.getInsertSql();
		int update = this.update(con -> {
			// 设置返回的主键字段名
			PreparedStatement ps = con.prepareStatement(insertSql.getSql(),
					Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < insertSql.getArgs().size(); i++) {
				ps.setObject(i + 1, insertSql.getArgs().get(i));
			}
			return ps;
		}, keyHolder);
		// 主键id反射赋值
		String fieldName = insertSql.getEntityInfo().getIdFieldInfo().getFieldName();
		if (CollectionUtil.isNotEmpty(keyHolder.getKeyList())
				&& null != insertSql.getEntityInfo().getIdFieldInfo()
				&& null != keyHolder.getKey()
				&& null != entity) {
			ReflectUtil.setFieldValue(entity, fieldName, keyHolder.getKey().longValue());
		}
		return update;
	}

	/**
	 * 根据id修改
	 *
	 * @param entity
	 * @return int
	 * @since 2021/3/9
	 */
	public <T> int updateById(T entity) {
		EntityWrappers entityWrappers = getEntityWrappers(entity);
		EntityWrappers.SqlResult updateSql = entityWrappers.getUpdateSql();
		String sql = updateSql.getSql();
		EntityInfo entityInfo = entityWrappers.getEntityInfo();

		sql = sql + " WHERE " + SqlKeyword.EQ.getSqlSegment(entityInfo.getIdFieldInfo().getJdbcField());
		updateSql.getArgs().add(entityInfo.getIdFieldInfo().getFieldValue());
		int update = this.update(sql, updateSql.args());
		return update;
	}

	/**
	 * 根据 whereEntity 条件，更新记录
	 *
	 * @param entity        实体对象 (set 条件值,可以为 null)
	 * @param entityWrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
	 * @return int
	 * @since 2021/3/9
	 */
	public <T> int update(T entity, EntityWrapper<T> entityWrapper) {
		// 查询的SQL
		EntityWrappers.SqlResult updateSql = getEntityWrappers(entity).getUpdateSql();
		// 条件
		StringBuilder sql = new StringBuilder();
		sql.append(updateSql.getSql());
		EntityWrappers<T> whereWrapper = getEntityWrappers(entityWrapper);
		whereWrapper.setWhereSql(sql, this.jdbcGlobalConfig.getUpdateStrategy(), false);

		List args = updateSql.getArgs();
		args.addAll(entityWrapper.getArgs());
		int update = this.update(sql.toString(), args.toArray());
		return update;
	}

	/**
	 * 根据id进行删除
	 *
	 * @param id id
	 * @return int
	 * @since 2021/3/9
	 */
	public <T> int deleteById(Serializable id, Class<T> requiredType) {

		EntityWrappers entityWrappers = getEntityWrappers(requiredType);
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(entityWrappers.getEntityInfo().getTableName());
		sql.append(" WHERE "
				+ SqlKeyword.EQ.getSqlSegment(entityWrappers.getEntityInfo().getIdFieldInfo().getJdbcField()));
		int update = this.update(sql.toString(), id);
		return update;
	}

	/**
	 * 根据构造条件删除
	 *
	 * @param wrapper
	 * @return int
	 * @since 2021/3/9
	 */
	public <T> int delete(EntityWrapper<T> wrapper) {
		EntityWrappers<T> entityWrappers = getEntityWrappers(wrapper);
		EntityWrappers.SqlResult deleteSql = entityWrappers.getDeleteSql();
		return this.update(deleteSql.getSql(), deleteSql.args());
	}

	/**
	 * 根据id集合删除
	 *
	 * @param idList
	 * @return int
	 * @since 2021/3/9
	 */
	public <T> int deleteBatchIds(Collection<? extends Serializable> idList, Class<T> requiredType) {

		EntityWrappers entityWrappers = getEntityWrappers(requiredType);
		EntityWrapper entityWrapper = entityWrappers.getEntityWrapper();
		entityWrapper.in(CollectionUtil.isNotEmpty(idList),
				entityWrappers.getEntityInfo().getIdFieldInfo().getJdbcField(), idList);
		entityWrappers.setEntityWrapper(entityWrapper);
		EntityWrappers.SqlResult deleteSql = entityWrappers.getDeleteSql();
		return this.update(deleteSql.getSql(), deleteSql.args());
	}

	/**
	 * 分页查询
	 *
	 * @param offset 下标
	 * @param limit  条数
	 * @param entity 实体条件
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.page.PageInfo<T>
	 * @since 2021/3/14
	 */
	public <T> PageInfo<T> selectByPage(Long offset, Long limit, T entity) {

		return selectByPage(offset, limit, entity, null);
	}

	/**
	 * 分页查询
	 *
	 * @param offset 下标
	 * @param limit  条数
	 * @param entity 实体条件
	 * @param orders 排序条件
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.page.PageInfo<T>
	 * @since 2021/3/14
	 */
	public <T> PageInfo<T> selectByPage(Long offset, Long limit, T entity, List<OrderItem> orders) {

		PageInfo<T> pageInfo = new PageInfo<>();
		List<T> contentList = new ArrayList<>();
		EntityWrapper<T> countWrapper = new EntityWrapper<>(entity);
		Integer count = this.selectCount(countWrapper);
		if (null != count && count > 0) {
			EntityWrappers.SqlResult sqlResult = getEntityWrappers(entity).getSelectSql();
			String sql = addOrderBySql(sqlResult.getSql(), orders) + " limit " + offset + "," + limit;

			contentList = this.query(sql, new BeanPropertyRowMapper<T>((Class<T>) entity.getClass()), sqlResult.args());
			pageInfo.setTotalElements(Long.valueOf(count));
		}

		pageInfo.setContent(contentList);
		pageInfo.setPageNum((offset / limit) + 1);
		pageInfo.setPageSize(limit);

		return pageInfo;
	}

	/**
	 * 添加排序的SQL
	 *
	 * @param sql
	 * @param orderItems
	 * @return java.lang.String
	 * @since 2021/6/27
	 */
	private String addOrderBySql(String sql, List<OrderItem> orderItems) {

		if (CollectionUtil.isEmpty(orderItems)) {
			return sql;
		}
		return sql + " ORDER BY " + orderItems.stream().map(a -> a.getColumn() + " " + (a.isAsc() ? "ASC" : "DESC"))
				.collect(Collectors.joining(","));
	}

	/**
	 * 分页查询
	 *
	 * @param pageRequestParams
	 * @param requiredType
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.page.PageInfo<T>
	 * @since 2021/3/14
	 */
	public <T> PageInfo<T> selectByPage(PageRequestParams<T> pageRequestParams, Class<T> requiredType) {
		T entity = pageRequestParams.getParams();
		if (null == entity) {
			try {
				entity = requiredType.newInstance();
				pageRequestParams.setParams(entity);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return selectByPage(pageRequestParams.getOffset(), pageRequestParams.getPageSize(),
				pageRequestParams.getParams(), pageRequestParams.getOrders());

	}

}
