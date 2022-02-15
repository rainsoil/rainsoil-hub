package com.rainsoil.common.security.core.core;

import com.rainsoil.common.framework.spring.SpringContextHolder;
import lombok.experimental.UtilityClass;

/**
 * 获取登录的用户信息的静态类
 *
 * @author luyanan
 * @since 2021/10/4
 **/
@UtilityClass
public class LoginUserServices {

	/**
	 * 获取登录用户
	 * @param required 是否必须
	 * @return com.rainsoil.fastjava.security.core.LoginUserDetail
	 * @since 2021/10/3
	 */
	public LoginUserDetail getUser(boolean required) {
		return SpringContextHolder.getBean(LoginUserService.class).getUser(required);
	}

	/**
	 * 获取登录用户
	 * @return com.rainsoil.fastjava.security.core.LoginUserDetail
	 * @since 2021/10/3
	 */
	public LoginUserDetail getUser() {
		return SpringContextHolder.getBean(LoginUserService.class).getUser();
	}

	/**
	 * 获取用户id
	 * @param required 是否必须
	 * @return java.lang.String
	 * @since 2021/10/3
	 */
	public String getUserId(boolean required) {
		return SpringContextHolder.getBean(LoginUserService.class).getUserId(required);
	}

	/**
	 * 获取用户id
	 * @return java.lang.String
	 * @since 2021/10/3
	 */
	public String getUserId() {
		return SpringContextHolder.getBean(LoginUserService.class).getUserId();
	}

}
