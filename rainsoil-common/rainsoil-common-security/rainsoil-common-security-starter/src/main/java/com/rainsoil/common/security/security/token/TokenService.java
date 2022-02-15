package com.rainsoil.common.security.security.token;

import org.springframework.security.core.Authentication;

/**
 * token的service
 *
 * @author luyanan
 * @since 2021/10/10
 **/
public interface TokenService {

	/**
	 * 读取token
	 * @param token
	 * @return com.rainsoil.fastjava.security.core.LoginUserDetail
	 * @since 2021/10/10
	 */
	Authentication readToken(String token);

	/**
	 * 生成token
	 * @param authentication 用户信息
	 * @return com.rainsoil.fastjava.security.security.token.AccessToken
	 * @since 2021/10/10
	 */
	AccessToken getAccessToken(Authentication authentication);

	/**
	 * 校验token
	 * @param accessToken
	 * @return boolean
	 * @since 2021/10/10
	 */
	boolean validateAccessToken(String accessToken);

	/**
	 * 移除token
	 * @param token token
	 * @return void
	 * @since 2021/10/10
	 */
	void removeToken(String token);

	/**
	 * 刷新token
	 * @param refreshToken token
	 * @return com.rainsoil.fastjava.security.security.token.AccessToken
	 * @since 2021/10/10
	 */
	AccessToken refreshToken(String refreshToken);

	/**
	 * 校验刷新的token
	 * @param refreshToken
	 * @return boolean
	 * @since 2021/10/10
	 */
	boolean validateRefreshToken(String refreshToken);

}
