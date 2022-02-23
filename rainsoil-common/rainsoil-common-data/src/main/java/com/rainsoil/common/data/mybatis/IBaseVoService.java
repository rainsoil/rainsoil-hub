package com.rainsoil.common.data.mybatis;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;

import java.io.Serializable;

/**
 * service抽象类
 *
 * @author luyanan
 * @since 2021/12/9
 **/
public interface IBaseVoService<T, V> extends IService<T> {


	/**
	 * 分页查询
	 *
	 * @param requestParam 分页参数
	 * @return com.rainsoil.common.core.page.PageInfo<V>
	 * @since 2022/2/8
	 */
	PageInfo<V> findByPage(PageRequestParams<V> requestParam);


	/**
	 * 根据id查询
	 *
	 * @param id 主键id
	 * @return V
	 * @since 2021/12/9
	 */
	V findById(Serializable id);


	/**
	 * 插入操作
	 *
	 * @param vo vo类
	 * @return boolean
	 * @since 2021/12/9
	 */
	T insert(V vo);


	/**
	 * 根据id修改
	 *
	 * @param v
	 * @return boolean
	 * @since 2021/12/9
	 */
	boolean update(V v);


	/**
	 * 根据id删除
	 *
	 * @param id 主键id
	 * @return boolean
	 * @since 2021/12/9
	 */
	boolean deleteById(Long id);

}

