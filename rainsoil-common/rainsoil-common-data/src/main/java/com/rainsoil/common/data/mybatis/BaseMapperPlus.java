package com.rainsoil.common.data.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author luyanan
 * @since 2022/2/9
 **/
public interface BaseMapperPlus<T> extends BaseMapper<T> {
	/**
	 * 根据 id 逻辑删除数据,并带字段填充功能
	 *
	 * @param entity
	 * @return int
	 * @since 2021/9/17
	 */
	int deleteByIdWithFill(T entity);

}
