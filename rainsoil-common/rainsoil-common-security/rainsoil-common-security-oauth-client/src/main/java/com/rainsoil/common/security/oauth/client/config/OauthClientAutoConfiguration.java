package com.rainsoil.common.security.oauth.client.config;

import com.rainsoil.common.security.oauth.core.client.SpringSecurityOauthClientProperties;
import com.rainsoil.common.security.oauth.core.token.MyBearerTokenExtractor;
import com.rainsoil.common.security.oauth.core.token.TokenConfig;
import com.rainsoil.common.security.security.annotation.EnableSecurity;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Oauth2.0 客户端的自动配置类
 *
 * @author luyanan
 * @since 2021/10/25
 **/
@EnableSecurity
@Configuration
@EnableConfigurationProperties(SpringSecurityOauthClientProperties.class)
@Import({TokenConfig.class, ResourceServerConfig.class})
public class OauthClientAutoConfiguration {

	/**
	 * myBearerTokenExtractor
	 *
	 * @return com.rainsoil.common.security.oauth.core.token.MyBearerTokenExtractor
	 * @since 2022/3/6
	 */
	@Bean
	public MyBearerTokenExtractor myBearerTokenExtractor() {
		return new MyBearerTokenExtractor();
	}

}
