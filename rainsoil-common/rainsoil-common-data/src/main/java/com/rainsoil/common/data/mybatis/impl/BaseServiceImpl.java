package com.rainsoil.common.data.mybatis.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.data.mybatis.Conver;
import com.rainsoil.common.data.mybatis.IBaseService;
import com.rainsoil.common.data.mybatis.ListConver;

import java.io.Serializable;
import java.util.List;

/**
 * serviceImpl 抽象类
 *
 * @author luyanan
 * @since 2021/12/9
 **/
public class BaseServiceImpl<M extends BaseMapper<T>, T, V> extends ServiceImpl<M, T> implements IBaseService<T, V> {

	@Override
	protected Class<T> currentMapperClass() {
		return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
	}

	@Override
	protected Class<T> currentModelClass() {
		return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
	}

	protected Class<V> currentVoClass() {
		return (Class<V>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
	}


	/**
	 * 分页查询
	 *
	 * @param requestParam 分页参数
	 * @return com.petecc.reguator.core.PageInfo<V>
	 * @since 2021/12/9
	 */
	@Override
	public PageInfo<V> findByPage(PageRequestParams<V> requestParam) {
		return findByPage(requestParam, new ListConver<T, V>() {
		});
	}



	/**
	 * 分页查询
	 *
	 * @param requestParam 分页参数
	 * @param listConver   列表转换
	 * @return com.petecc.reguator.core.PageInfo<V>
	 * @since 2021/12/9
	 */
	protected PageInfo<V> findByPage(PageRequestParams<V> requestParam, ListConver<T, V> listConver) {
		V param = requestParam.getParams();
		T t = new Conver<V, T>() {
		}.conver(param, currentModelClass());
		LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>(t);
		return findByPage(requestParam, queryWrapper, listConver);
	}

	/**
	 * 分页查询
	 *
	 * @param requestParam 分页参数
	 * @param queryWrapper 条件构造
	 * @param listConver   列表转换
	 * @return com.petecc.reguator.core.PageInfo<V>
	 * @since 2021/12/9
	 */
	protected PageInfo<V> findByPage(PageRequestParams<V> requestParam, LambdaQueryWrapper<T> queryWrapper,
									 ListConver<T, V> listConver) {
		IPage<T> page = new Page<>(requestParam.getPageNum(), requestParam.getPageSize());
		page = this.page(page, queryWrapper);
		PageInfo<V> pageInfo = new PageInfo<>();
		pageInfo.setPageSize(page.getSize());
		pageInfo.setTotalPages(page.getPages());
		pageInfo.setPageNum(page.getCurrent());
		pageInfo.setTotalElements(page.getCurrent());
		List<V> vList = listConver.conver(page.getRecords(), currentVoClass());
		pageInfo.setContent(vList);
		return pageInfo;
	}




	/**
	 * 根据id查询
	 *
	 * @param id 主键id
	 * @return V
	 * @since 2021/12/9
	 */
	@Override
	public V findById(Serializable id) {
		return findById(id, new Conver<T, V>() {
		});
	}

	public V findById(Serializable id, Conver<T, V> conver) {
		T t = this.getById(id);
		V v = conver.conver(t, currentVoClass());
		return v;
	}

	/**
	 * 插入操作
	 *
	 * @param vo vo类
	 * @return boolean
	 * @since 2021/12/9
	 */
	@Override
	public T insert(V vo) {
		return insert(vo, new Conver<V, T>() {
		});
	}

	protected T insert(V vo, Conver<V, T> conver) {
		T t = conver.conver(vo, currentModelClass());
		this.save(t);
		return t;
	}

	/**
	 * 根据id修改
	 *
	 * @param v
	 * @return boolean
	 * @since 2021/12/9
	 */
	@Override
	public boolean update(V v) {
		return update(v, new Conver<V, T>() {
		});
	}

	protected boolean update(V vo, Conver<V, T> conver) {
		T t = conver.conver(vo, currentModelClass());
		return this.updateById(t);
	}

	/**
	 * 根据id删除
	 *
	 * @param id 主键id
	 * @return boolean
	 * @since 2021/12/9
	 */
	@Override
	public boolean deleteById(Long id) {
		return this.removeById(id);
	}


}
