package com.rainsoil.common.data.mybatis.impl;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.data.mybatis.BaseMapperPlus;
import com.rainsoil.common.data.mybatis.IBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * 基本service实现类
 *
 * @param <T> 实体
 * @param <M> baseMapper
 * @author luyanan
 * @since 2022/2/20
 **/
public class BaseServiceImpl<M extends BaseMapperPlus<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

	/**
	 * 分页查询
	 *
	 * @param requestParams 分页参数
	 * @return com.rainsoil.common.core.page.PageInfo<T>
	 * @since 2022/2/20
	 */
	@Override
	public PageInfo<T> page(PageRequestParams<T> requestParams) {
		LambdaQueryWrapper<T> queryWrapper = converPageRequestParams(requestParams.getParams());
		return page(requestParams, queryWrapper);
	}

	/**
	 * 实体作为条件查询
	 *
	 * @param entity 条件
	 * @return java.util.List<T>
	 * @since 2022/2/21
	 */
	@Override
	public List<T> list(T entity) {
		return this.list(new LambdaQueryWrapper<>(entity));
	}

	/**
	 * 根据条件查询单条
	 *
	 * @param entity 实体条件
	 * @return T
	 * @since 2022/2/21
	 */
	@Override
	public T getOne(T entity) {
		return this.getOne(new LambdaQueryWrapper<>(entity));
	}

	/**
	 * 根据实体条件删除
	 *
	 * @param entity 实体条件
	 * @return boolean
	 * @since 2022/2/21
	 */
	@Override
	public boolean remove(T entity) {
		return this.remove(new LambdaQueryWrapper<>(entity));
	}

	/**
	 * 根据实体条件统计
	 *
	 * @param entity 实体类
	 * @return int
	 * @since 2022/2/21
	 */
	@Override
	public long count(T entity) {
		return this.count(new LambdaQueryWrapper<>(entity));
	}


	/**
	 * 分页查询
	 *
	 * @param requestParams 分页参数
	 * @param queryWrapper  条件构造
	 * @return com.rainsoil.common.core.page.PageInfo<T>
	 * @since 2022/3/6
	 */
	protected PageInfo<T> page(PageRequestParams<T> requestParams, LambdaQueryWrapper<T> queryWrapper) {
		IPage page = this.page(converIPage(requestParams), queryWrapper);
		return converPageInfo(page, requestParams);
	}

	/**
	 * 转换成IPage对象
	 *
	 * @param pageRequestParams 分页查询参数
	 * @return com.baomidou.mybatisplus.core.metadata.IPage
	 * @since 2021/9/29
	 */
	protected IPage converIPage(PageRequestParams<T> pageRequestParams) {
		IPage page = new Page(pageRequestParams.getPageNum(), pageRequestParams.getPageSize());
		return page;
	}

	/**
	 * 转换为PageInfo对象
	 *
	 * @param page          page
	 * @param requestParams 分页查询参数
	 * @return PageInfo<T>
	 * @since 2021/9/29
	 */
	protected PageInfo<T> converPageInfo(IPage page, PageRequestParams<T> requestParams) {
		return new PageInfo<T>(page.getRecords(), page.getTotal(), requestParams);
	}

	/**
	 * 转换为LambdaQueryWrapper 对象
	 *
	 * @param param 参数
	 * @return com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T>
	 * @since 2022/2/20
	 */
	protected LambdaQueryWrapper<T> converPageRequestParams(T param) {
		return new LambdaQueryWrapper<>(param);
	}


	@Override
	public boolean removeById(Serializable id) {
		Class<T> modelClass = currentModelClass();
		T instance = null;
		try {
			instance = modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		String keyColumn = SqlHelper.table(modelClass).getKeyProperty();
		ReflectUtil.setFieldValue(instance, keyColumn, id);
		return this.baseMapper.deleteByIdWithFill(instance) > 0;
	}

}
