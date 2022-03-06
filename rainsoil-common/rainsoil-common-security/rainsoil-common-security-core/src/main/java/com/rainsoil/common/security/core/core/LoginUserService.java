package com.rainsoil.common.security.core.core;

import cn.hutool.core.bean.OptionalBean;

/**
 * 获取登录用户的service
 *
 * @author luyanan
 * @since 2021/10/3
 **/
public interface LoginUserService {

	/**
	 * 获取登录用户
	 *
	 * @param required 是否必须
	 * @return com.rainsoil.security.core.LoginUserDetail
	 * @since 2021/10/3
	 */
	LoginUserDetail getUser(boolean required);

	/**
	 * 获取登录用户
	 *
	 * @return com.rainsoil.security.core.LoginUserDetail
	 * @since 2021/10/3
	 */
	default LoginUserDetail getUser() {
		return getUser(true);
	}

	/**
	 * 获取用户id
	 *
	 * @param required 是否必须
	 * @return java.lang.String
	 * @since 2021/10/3
	 */
	default String getUserId(boolean required) {
		return OptionalBean.ofNullable(getUser(required)).getBean(LoginUserDetail::getUserId).orElse(null);

	}

	/**
	 * 获取用户id
	 *
	 * @return java.lang.String
	 * @since 2021/10/3
	 */
	default String getUserId() {
		return getUser(true).getUserId();
	}


	/**
	 * 锁定
	 *
	 * @since 2022/2/20
	 */
	void lock();


	/**
	 * 解锁
	 *
	 * @since 2022/2/20
	 */
	void unLock();
}
