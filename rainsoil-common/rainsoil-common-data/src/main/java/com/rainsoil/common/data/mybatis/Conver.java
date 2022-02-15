package com.rainsoil.common.data.mybatis;


import org.springframework.beans.BeanUtils;

/**
 * @author luyanan
 * @since 2021/12/9
 **/
public interface Conver<T, V> {

	/**
	 * T ->类转换
	 *
	 * @param t
	 * @param v
	 * @return void
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
	 * @param t
	 * @param voClass
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
	 * @param t
	 * @param v
	 * @return void
	 * @since 2021/12/22
	 */
	default void converHandler(T t, V v) {
	}
}
