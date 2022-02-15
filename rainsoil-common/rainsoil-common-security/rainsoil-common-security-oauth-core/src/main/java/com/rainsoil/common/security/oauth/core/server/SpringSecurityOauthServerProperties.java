package com.rainsoil.common.security.oauth.core.server;

import com.rainsoil.common.security.oauth.core.config.SpringSecurityOauthProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Oauth 服务端配置
 *
 * @author luyanan
 * @since 2021/10/26
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = SpringSecurityOauthServerProperties.PREFIX)
public class SpringSecurityOauthServerProperties extends SpringSecurityOauthProperties {

	public static final String PREFIX = SpringSecurityOauthProperties.PREFIX + ".server";

	/**
	 * 刷新令牌的默认有效时间
	 *
	 * @since 2021/10/26
	 */

	private Duration refreshTokenValidity = Duration.ofHours(1);

	/**
	 * 令牌的默认有效时间
	 *
	 * @since 2021/10/26
	 */

	private Duration accessTokenValidity = Duration.ofHours(6);

}
