package com.rainsoil.common.security.core;

import cn.hutool.core.collection.CollectionUtil;
import com.rainsoil.common.security.core.annotation.IgnoringLogin;
import com.rainsoil.common.security.security.config.SecurityProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 扫描忽略登录的注解
 *
 * @author luyanan
 * @since 2021/10/5
 **/
public class IgnoringLoginScanner implements InitializingBean {

	private Set<String> ignoringLoginUrl = new HashSet<>();

	@Autowired(required = false)
	private SecurityProperties securityProperties;

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Override
	public void afterPropertiesSet() {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		if (CollectionUtil.isNotEmpty(handlerMethods)) {
			for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
				HandlerMethod method = entry.getValue();
				if (method.hasMethodAnnotation(IgnoringLogin.class)
						|| method.getMethod().getDeclaringClass().isAnnotationPresent(IgnoringLogin.class)) {
					Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
					ignoringLoginUrl.addAll(patterns);

				}
			}
		}
	}

	public Set<String> getIgnoringLoginUrl() {
		if (null != securityProperties && CollectionUtil.isNotEmpty(securityProperties.getIgnoringLoginUrl())) {
			ignoringLoginUrl.addAll(securityProperties.getIgnoringLoginUrl());
		}
		return ignoringLoginUrl;
	}

}
