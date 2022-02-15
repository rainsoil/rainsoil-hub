package com.rainsoil.common.security.oauth.server;

import com.rainsoil.common.security.oauth.core.server.SpringSecurityOauthServerProperties;
import com.rainsoil.common.security.oauth.server.exception.CustomWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 授权服务端的配置
 *
 * @author luyanan
 * @since 2021/10/26
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private SpringSecurityOauthServerProperties serverProperties;

	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthorizationCodeServices authorizationCodeServices;

	@Autowired
	private CustomWebResponseExceptionTranslator exceptionTranslator;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Bean()
	public AuthorizationServerTokenServices authorizationServerTokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setClientDetailsService(clientDetailsService);
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setTokenStore(tokenStore);

		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
		tokenServices.setTokenEnhancer(tokenEnhancerChain);
		// 令牌的默认有效时间 2小时
		Assert.notNull(serverProperties.getAccessTokenValidity(), "令牌的默认有效时间不能为空");
		Assert.notNull(serverProperties.getRefreshTokenValidity(), "令牌的刷新令牌的有效时间不能为空");
		tokenServices.setAccessTokenValiditySeconds((int) serverProperties.getAccessTokenValidity().getSeconds());
		// 刷新令牌的默认有效时间3天
		tokenServices.setRefreshTokenValiditySeconds((int) serverProperties.getRefreshTokenValidity().getSeconds());
		return tokenServices;
	}

	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		// 设置授权码模式的授权码如何存储, 暂时采用内存存储的方式
		// return new InMemoryAuthorizationCodeServices();
		return new JdbcAuthorizationCodeServices(dataSource);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.withClientDetails(clientDetailsService);
		// clients.inMemory()// 使用in‐memory存储
		// .withClient("c1")// client_id
		// .secret(new BCryptPasswordEncoder().encode("secret"))
		// .resourceIds("res1")
		// .authorizedGrantTypes("authorization_code",
		// "password", "client_credentials", "implicit", "refresh_token")// 该client允许的授权类型
		// authorization_code,password,refresh_token,implicit,client_credentials
		// .scopes("all")// 允许的授权范围
		// .autoApprove(false)
		//// 加上验证回调地址
		// .redirectUris("http://www.baidu.com");

	}

	/**
	 * 令牌端点安全约束
	 * @param security
	 * @return void
	 * @since 2021/2/19
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()").allowFormAuthenticationForClients();
	}

	/**
	 * Spring security5中新增加了加密方式，并把原有的spring security的密码存储格式改了
	 * @param endpoints
	 * @throws Exception
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager).authorizationCodeServices(authorizationCodeServices)
				.tokenServices(authorizationServerTokenServices()).allowedTokenEndpointRequestMethods(HttpMethod.POST)
				.exceptionTranslator(exceptionTranslator);
	}

}
