package com.rainsoil.common.data.mybatis;


import org.springframework.beans.BeanUtils;

/**
 * @param <T> 实体泛型
 * @param <V> vo泛型
 * @author luyanan
 * @since 2021/12/9
 **/
public interface Conver<T, V> {

	/**
	 * T ->类转换
	 *
	 * @param t 实体
	 * @param v vo
	 * @return V
	 * @since 2021/12/9
	 */
	default V conver(T t, V v) {

		if (t == null) {
			return null;
		}
		BeanUtils.copyProperties(t, v);
		converHandler(t, v);
		return v;
	}


	/**
	 * 转换
	 *
	 * @param t       实体
	 * @param voClass vo类class
	 * @return V
	 * @since 2021/12/9
	 */


	default V conver(T t, Class<? extends V> voClass) {
		if (null == voClass) {
			return null;
		}
		return conver(t, BeanUtils.instantiateClass(voClass));
	}


	/**
	 * 自定义处理
	 *
	 * @param t 实体
	 * @param v vo
	 * @since 2021/12/22
	 */
	default void converHandler(T t, V v) {
	}
}
