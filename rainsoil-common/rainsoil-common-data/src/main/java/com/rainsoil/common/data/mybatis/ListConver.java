package com.rainsoil.common.data.mybatis;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 列表转换
 *
 * @author luyanan
 * @since 2021/12/9
 **/
public interface ListConver<T, V> {


	/**
	 * 列表转换
	 *
	 * @param ts  ts
	 * @param vos vos
	 * @return java.util.List<V>
	 * @since 2021/12/9
	 */
	default List<V> conver(List<T> ts, List<V> vos) {
		if (CollectionUtil.isEmpty(ts)) {
			return new ArrayList<>();
		}

		return converHandler(ts, vos);
	}


	/**
	 * 转换
	 *
	 * @param ts      ts
	 * @param voClass vo类class
	 * @return java.util.List<V>
	 * @since 2022/2/9
	 */
	default List<V> conver(List<T> ts, Class<? extends V> voClass) {
		if (CollectionUtil.isEmpty(ts) || null == voClass) {
			return new ArrayList<>();
		}
		List<V> vList = ts.stream().map(t -> {
			V v = BeanUtils.instantiateClass(voClass);
			BeanUtils.copyProperties(t, v);
			return v;
		}).collect(Collectors.toList());
		return conver(ts, vList);
	}


	/**
	 * 列表处理
	 *
	 * @param ts
	 * @param vos
	 * @return void
	 * @since 2021/12/22
	 */
	default List<V> converHandler(List<T> ts, List<V> vos) {
		return vos;
	}
}
