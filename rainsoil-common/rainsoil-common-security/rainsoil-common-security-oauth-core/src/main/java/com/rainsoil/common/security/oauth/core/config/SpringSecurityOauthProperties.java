package com.rainsoil.common.security.oauth.core.config;

import com.rainsoil.common.security.security.config.SecurityProperties;
import lombok.Data;

/**
 * 配置文件
 *
 * @author luyanan
 * @since 2021/10/25
 **/
@Data
public class SpringSecurityOauthProperties {

	public static final String PREFIX = SecurityProperties.PREFIX + ".oauth";

	/**
	 * 使用token令牌的时候的资源服务密钥
	 *
	 * @since 2021/10/25
	 */

	private String tokenSigningKey;

}
