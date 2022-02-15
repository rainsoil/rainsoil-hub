package com.rainsoil.common.security.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainsoil.common.security.core.IgnoringLoginScanner;
import com.rainsoil.common.security.core.LoginUserServiceImpl;
import com.rainsoil.common.security.core.core.LoginUserService;
import com.rainsoil.common.security.core.core.PermissionService;
import com.rainsoil.common.security.security.WebSecurityConfig;
import com.rainsoil.common.security.security.token.JwtTokenServiceImpl;
import com.rainsoil.common.security.security.token.TokenService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全模块配置类
 *
 * @author luyanan
 * @since 2021/10/6
 **/

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@Import(WebSecurityConfig.class)
public class WebSecurityAutoConfiguration {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public IgnoringLoginScanner ignoringLoginScanner() {
		return new IgnoringLoginScanner();
	}

	@Bean("ss")
	public PermissionService permissionService() {
		return new PermissionService();
	}

	@Bean
	public LoginUserService loginUserService() {
		return new LoginUserServiceImpl();
	}

	@Bean
	public TokenService tokenService(SecurityProperties securityProperties, ObjectMapper objectMapper) {
		return new JwtTokenServiceImpl(objectMapper,securityProperties.getJwt());
	}

}
