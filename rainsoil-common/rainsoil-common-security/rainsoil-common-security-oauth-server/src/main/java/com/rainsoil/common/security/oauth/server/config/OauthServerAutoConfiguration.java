package com.rainsoil.common.security.oauth.server.config;

import com.rainsoil.common.security.oauth.core.server.SpringSecurityOauthServerProperties;
import com.rainsoil.common.security.oauth.core.token.TokenConfig;
import com.rainsoil.common.security.oauth.server.AuthorizationServer;
import com.rainsoil.common.security.oauth.server.exception.CustomWebResponseExceptionTranslator;
import com.rainsoil.common.security.security.annotation.EnableSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * Oauth 服务端的自动配置
 *
 * @author luyanan
 * @since 2021/10/26
 **/
@Configuration
@EnableConfigurationProperties(SpringSecurityOauthServerProperties.class)
@EnableSecurity
@Import({ TokenConfig.class, AuthorizationServer.class })
public class OauthServerAutoConfiguration {

	@Bean
	public CustomWebResponseExceptionTranslator exceptionTranslator() {
		return new CustomWebResponseExceptionTranslator();
	}

	// 默认处于安全，会把UsernameNotFoundException转为BadCredentialsException，就是 “坏的凭据”，注入下面配置的bean
	@Bean
	public AuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider impl = new DaoAuthenticationProvider();
		impl.setUserDetailsService(userDetailsService);
		impl.setHideUserNotFoundExceptions(false);
		return impl;
	}

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Primary
	@Bean
	public ClientDetailsService clientDetailsService() {
		ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
		((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
		return clientDetailsService;
	}

}
