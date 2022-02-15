package com.rainsoil.common.security.security.config;

import lombok.Data;

import java.time.Duration;

/**
 * jwt的相关配置文件
 *
 * @author luyanan
 * @since 2021/10/7
 **/
@Data
public class JwtProperties {

	/**
	 * 标识
	 *
	 * @since 2021/10/7
	 */
	private String header = "Authorization";

	/**
	 * 加密的密钥
	 *
	 * @since 2021/10/7
	 */

	private String secret = "admin123";

	/**
	 * 有效期
	 *
	 * @since 2021/10/7
	 */

	private Duration expireTime = Duration.ofDays(1);

	/**
	 * 有效期
	 *
	 * @since 2021/10/7
	 */

	private Duration refreshTokenExpireTime = Duration.ofDays(1);

	/**
	 * token的开头
	 *
	 * @since 2021/10/7
	 */

	private String tokenHead = "Bearer ";

}
