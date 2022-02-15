package com.rainsoil.common.security.security;

import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.security.core.IgnoringLoginScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedList;
import java.util.List;

/**
 * 安全配置
 *
 * @author luyanan
 * @since 2021/10/5
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private IgnoringLoginScanner ignoringLoginScanner;

	@Autowired(required = false)
	private HttpSecurityConfigHandler httpSecurityConfigHandler;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		List<String> antMatchers = new LinkedList<>();
		for (String s : ignoringLoginScanner.getIgnoringLoginUrl()) {
			if (StrUtil.isBlank(s)) {
				continue;
			}
			antMatchers.add(s);
		}
		http.authorizeRequests().antMatchers(antMatchers.toArray(new String[antMatchers.size()])).permitAll()
				.anyRequest().authenticated();
		if (null != httpSecurityConfigHandler) {
			httpSecurityConfigHandler.configure(http);
		}
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		if (null != httpSecurityConfigHandler) {
			httpSecurityConfigHandler.configure(web);
		}
	}

	/**
	 * 身份认证接口
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	/**
	 * 解决 无法直接注入 AuthenticationManager
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
