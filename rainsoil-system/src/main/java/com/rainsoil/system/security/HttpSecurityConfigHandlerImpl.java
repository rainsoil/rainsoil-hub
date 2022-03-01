package com.rainsoil.system.security;

import com.rainsoil.common.security.security.HttpSecurityConfigHandler;
import com.rainsoil.common.security.security.config.SecurityProperties;
import com.rainsoil.common.security.security.token.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

/**
 * 安全访问实现
 *
 * @author luyanan
 * @since 2021/10/7
 **/
@Slf4j
@Component
public class HttpSecurityConfigHandlerImpl implements HttpSecurityConfigHandler {

	@Autowired
	LoginFailureHandler loginFailureHandler;

	@Autowired
	LoginSuccessHandler loginSuccessHandler;


	@Autowired
	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	JwtAccessDeniedHandler jwtAccessDeniedHandler;


	@Autowired
	JWTLogoutSuccessHandler jwtLogoutSuccessHandler;

	@Autowired
	private AuthenticationManager authenticationManager;


	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private TokenService tokenService;

	public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
		return new JwtAuthenticationTokenFilter(authenticationManager, securityProperties, tokenService);
	}

	/**
	 * anyRequest          |   匹配所有请求路径
	 * access              |   SpringEl表达式结果为true时可以访问
	 * anonymous           |   匿名可以访问
	 * denyAll             |   用户不能访问
	 * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
	 * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
	 * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
	 * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
	 * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
	 * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
	 * permitAll           |   用户可以任意访问
	 * rememberMe          |   允许通过remember-me登录的用户访问
	 * authenticated       |   用户登录后可访问
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.headers()
				.frameOptions().sameOrigin().httpStrictTransportSecurity()
				.disable().and().cors().and()
//				.csrf().disable()
				// 登录配置
				.formLogin()
				.successHandler(loginSuccessHandler)
				.failureHandler(loginFailureHandler)

				.and()
				.logout()
				.logoutSuccessHandler(jwtLogoutSuccessHandler)

				// 禁用session
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				// 配置拦截规则
				.and()
				.authorizeRequests()
				// 异常处理器
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler)

				// 配置自定义的过滤器
				.and()
				.addFilter(jwtAuthenticationTokenFilter());
		// 验证码过滤器放在UsernamePassword过滤器之前
//				.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
	}


}
