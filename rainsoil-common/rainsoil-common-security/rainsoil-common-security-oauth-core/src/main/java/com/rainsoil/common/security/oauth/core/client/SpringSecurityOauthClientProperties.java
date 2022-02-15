package com.rainsoil.common.security.oauth.core.client;

import com.rainsoil.common.security.oauth.core.config.SpringSecurityOauthProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Oauth2.0 资源客户端的配置
 *
 * @author luyanan
 * @since 2021/10/25
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = SpringSecurityOauthClientProperties.PREFIX)
public class SpringSecurityOauthClientProperties extends SpringSecurityOauthProperties {

	public static final String PREFIX = SpringSecurityOauthProperties.PREFIX + ".client";

	/**
	 * 授权中心的地址
	 *
	 * @since 2021/10/25
	 */

	private String authorizationServerHost;

	/**
	 * 授权中心的服务名
	 *
	 * @since 2021/10/25
	 */

	private String authorizationServerName;

	/**
	 * 资源id
	 *
	 * @since 2021/10/25
	 */

	private String resourceId;

	/**
	 * 客户端id
	 *
	 * @since 2021/10/25
	 */

	private String clientId;

	/**
	 * 客户端密钥
	 *
	 * @since 2021/10/25
	 */

	private String clientSecret;

}
