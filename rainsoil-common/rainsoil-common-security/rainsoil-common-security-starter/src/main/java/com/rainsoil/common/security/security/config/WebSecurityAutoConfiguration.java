package com.rainsoil.common.security.security.config;

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

	/**
	 * 密码编解码
	 *
	 * @return org.springframework.security.crypto.password.PasswordEncoder
	 * @since 2022/3/6
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 忽略url扫描
	 *
	 * @return com.rainsoil.common.security.core.IgnoringLoginScanner
	 * @since 2022/3/6
	 */
	@Bean
	public IgnoringLoginScanner ignoringLoginScanner() {
		return new IgnoringLoginScanner();
	}

	/**
	 * 权限
	 *
	 * @return com.rainsoil.common.security.core.core.PermissionService
	 * @since 2022/3/6
	 */
	@Bean("ss")
	public PermissionService permissionService() {
		return new PermissionService();
	}

	/**
	 * 登录用户
	 *
	 * @return com.rainsoil.common.security.core.core.LoginUserService
	 * @since 2022/3/6
	 */
	@Bean
	public LoginUserService loginUserService() {
		return new LoginUserServiceImpl();
	}

	/**
	 * token 管理器
	 *
	 * @param securityProperties 配置文件
	 * @return com.rainsoil.common.security.security.token.TokenService
	 * @since 2022/3/6
	 */
	@Bean
	public TokenService tokenService(SecurityProperties securityProperties) {
		return new JwtTokenServiceImpl(securityProperties.getJwt());
	}

}
