package com.rainsoil.common.data.mybatis;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;

import java.util.List;

/**
 * 基本service
 *
 * @author luyanan
 * @since 2022/2/20
 **/
public interface IBaseService<T> extends IService<T> {


	/**
	 * 分页查询
	 *
	 * @param requestParams 分页参数
	 * @return com.rainsoil.common.core.page.PageInfo<T>
	 * @since 2022/2/20
	 */
	PageInfo<T> page(PageRequestParams<T> requestParams);


	/**
	 * 实体作为条件查询
	 *
	 * @param entity 条件
	 * @return java.util.List<T>
	 * @since 2022/2/21
	 */
	List<T> list(T entity);


	/**
	 * 根据条件查询单条
	 *
	 * @param entity 实体条件
	 * @return T
	 * @since 2022/2/21
	 */
	T getOne(T entity);


	/**
	 * 根据实体条件删除
	 *
	 * @param entity 实体条件
	 * @return boolean
	 * @since 2022/2/21
	 */
	boolean remove(T entity);


	/**
	 * 根据实体条件统计
	 *
	 * @param entity
	 * @return int
	 * @since 2022/2/21
	 */
	int count(T entity);
}
