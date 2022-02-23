package com.rainsoil.system.security;

import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.common.framework.spring.SpringContextHolder;
import com.rainsoil.common.security.security.HttpSecurityConfigHandler;
import com.rainsoil.core.message.GlobalCode;
import com.rainsoil.core.utils.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

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
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

//	@Autowired
//	private PermissFilter permissFilter;

	@Autowired
	private JwtAuthenticationTokenFilter authenticationTokenFilter;

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
	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler((request, response, authentication) -> {
//			log.debug("退出登录:{}", authentication.getName());
			SpringContextHolder.removeCookie(response, "Authorization", null);
			SpringContextHolder.removeCookie(response, "refreshToken", null);

			String clientId = SpringContextHolder.getCookieVal(request, "clientId");
			if (StrUtil.isNotBlank(clientId) && "web".equals(clientId)) {
				response.sendRedirect("/");
				response.setStatus(302);
			} else {
				response.setStatus(GlobalCode.SUCCESS);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				PrintWriter printWriter = response.getWriter();
				RespEntity success = RespEntity.success();
				printWriter.write(ObjectMapperUtils.getObjectMapper().writeValueAsString(success));
				printWriter.flush();
			}
		});
		httpSecurity
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
				// 由于使用的是JWT，我们这里不需要csrf
				.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				// 基于token，所以不需要session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


		// 禁用缓存
		httpSecurity.headers().cacheControl();

		// 添加JWT filter
		httpSecurity
				.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//		httpSecurity.addFilterAfter(permissFilter, FilterSecurityInterceptor.class);
	}


}
