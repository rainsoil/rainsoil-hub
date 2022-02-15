package com.rainsoil.common.security.security.token;

import lombok.Data;

/**
 * token
 *
 * @author luyanan
 * @since 2021/10/10
 **/
@Data
public class AccessToken {

	/**
	 * 范围
	 *
	 * @since 2021/10/10
	 */

	private String scope;

	/**
	 * token
	 *
	 * @since 2021/10/10
	 */

	private String value;

	/**
	 * 过期时间
	 *
	 * @since 2021/10/10
	 */

	private long expiresIn;

	/**
	 * 刷新的token
	 *
	 * @since 2021/10/10
	 */

	private RefreshToken refreshToken;

}
