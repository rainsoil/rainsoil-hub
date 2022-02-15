package com.rainsoil.common.security.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 * HttpSecurity 安全访问配置
 *
 * @author luyanan
 * @since 2021/10/5
 **/

public interface HttpSecurityConfigHandler {

	/**
	 * 安全配置
	 *
	 * @param http
	 * @throws Exception 异常
	 * @since 2021/10/5
	 */
	default void configure(HttpSecurity http) throws Exception {
	}

	/**
	 * 安全配置
	 *
	 * @param web
	 * @return void
	 * @since 2021/10/5
	 */
	default void configure(WebSecurity web) {

	}

}
