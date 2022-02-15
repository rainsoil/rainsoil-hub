package com.rainsoil.common.security.security.token;

import lombok.Data;

/**
 * 刷新token
 *
 * @author luyanan
 * @since 2021/10/10
 **/
@Data
public class RefreshToken {

	/**
	 * token
	 *
	 * @since 2021/10/10
	 */

	String value;

	/**
	 * 过期时间
	 *
	 * @since 2021/10/10
	 */
	private long expiresIn;

}
