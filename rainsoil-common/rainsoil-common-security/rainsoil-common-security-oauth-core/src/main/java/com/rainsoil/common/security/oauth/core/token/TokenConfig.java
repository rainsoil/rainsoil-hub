package com.rainsoil.common.security.oauth.core.token;

import cn.hutool.core.bean.OptionalBean;
import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.security.oauth.core.client.SpringSecurityOauthClientProperties;
import com.rainsoil.common.security.oauth.core.config.SpringSecurityOauthProperties;
import com.rainsoil.common.security.oauth.core.server.SpringSecurityOauthServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.Assert;

/**
 * 令牌配置
 *
 * @author luyanan
 * @since 2021/10/25
 **/
@Configuration
@ConditionalOnMissingBean(TokenConfig.class)
public class TokenConfig {

	@Autowired(required = false)
	private SpringSecurityOauthClientProperties clientProperties;

	@Autowired(required = false)
	private SpringSecurityOauthServerProperties serverProperties;

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	// @Bean
	// @Primary
	// public TokenService tokenService(TokenStore tokenStore,
	// AuthorizationServerTokenServices authorizationServerTokenServices) {
	// return new OauthTokenStore(authorizationServerTokenServices, tokenStore);
	// }

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {

		String tokenSigningKey = OptionalBean.ofNullable(clientProperties)
				.getBean(SpringSecurityOauthProperties::getTokenSigningKey).orElse(null);
		if (StrUtil.isBlank(tokenSigningKey)) {
			tokenSigningKey = OptionalBean.ofNullable(serverProperties)
					.getBean(SpringSecurityOauthProperties::getTokenSigningKey).orElse(null);
		}
		Assert.hasText(tokenSigningKey, "资源服务器密钥不能为空");
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		// 对称密钥,资源服务器使用该密钥进行验证
		converter.setSigningKey(tokenSigningKey);
		return converter;
	}

}
