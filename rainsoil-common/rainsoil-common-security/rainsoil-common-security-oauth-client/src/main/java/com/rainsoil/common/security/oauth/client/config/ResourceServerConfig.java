package com.rainsoil.common.security.oauth.client.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.security.core.IgnoringLoginScanner;
import com.rainsoil.common.security.oauth.client.exception.AuthExceptionEntryPoint;
import com.rainsoil.common.security.oauth.client.exception.CustomAccessDeniedHandler;
import com.rainsoil.common.security.oauth.core.client.SpringSecurityOauthClientProperties;
import com.rainsoil.common.security.oauth.core.token.MyBearerTokenExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.List;
import java.util.Set;

/**
 * 资源服务配置
 *
 * @author luyanan
 * @since 2021/10/25
 **/
@Configuration
@Slf4j
@SuppressWarnings(value = "all")
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private IgnoringLoginScanner ignoringLoginScanner;

	@Autowired
	private TokenStore tokenStore;

	@Autowired(required = false)
	private DiscoveryClient discoveryClient;

	@Autowired
	private MyBearerTokenExtractor myBearerTokenExtractor;

	@Autowired
	private SpringSecurityOauthClientProperties clientProperties;

	// /**
	// * 资源服务令牌解析服务
	// *
	// * @return
	// */
	// @Bean
	// public ResourceServerTokenServices tokenServices() {
	//
	// //使用远程服务请求授权服务器校验token , 必须指定校验token 的url,client_id,client_secret
	// RemoteTokenServices services = new RemoteTokenServices();
	// String authorizationServerHost = getAuthorizationServerHost();
	// Assert.hasText(authorizationServerHost, "授权服务器的host配置不能为空");
	// services.setCheckTokenEndpointUrl(authorizationServerHost +
	// "/uaa/oauth/check_token");
	// services.setClientId(securityOauthClientProperties.getClientId());
	// services.setClientSecret(securityOauthClientProperties.getClientSecret());
	// return services;
	// }

	/**
	 * 获取授权中心的地址 如果服务名配置并且是cloud环境,则通过授权服务名从注册中心获取, 否则通过authorizationServerHost 配置获取
	 * @return java.lang.String
	 * @since 2021/10/25
	 */
	private String getAuthorizationServerHost() {

		if (null != discoveryClient && StrUtil.isNotBlank(clientProperties.getAuthorizationServerName())) {
			List<ServiceInstance> instances = discoveryClient
					.getInstances(clientProperties.getAuthorizationServerName());
			if (CollectionUtil.isNotEmpty(instances)) {
				return instances.stream().map(ServiceInstance::getUri).findFirst().get().getPath();
			}
		}
		return clientProperties.getAuthorizationServerHost();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(clientProperties.getResourceId()).tokenStore(tokenStore)
				// .tokenServices(tokenServices())
				.tokenExtractor(myBearerTokenExtractor).stateless(true)
				.authenticationEntryPoint(new AuthExceptionEntryPoint())
				.accessDeniedHandler(new CustomAccessDeniedHandler());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		Set<String> ignoringLoginUrl = ignoringLoginScanner.getIgnoringLoginUrl();
		http.authorizeRequests().antMatchers(ignoringLoginUrl.toArray(new String[ignoringLoginUrl.size()])).permitAll();
	}

}
