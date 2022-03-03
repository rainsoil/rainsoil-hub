package com.rainsoil.common.framework.xss.config;

import com.rainsoil.common.framework.xss.core.FormXssClean;
import com.rainsoil.common.framework.xss.core.JacksonXssClean;
import com.rainsoil.common.framework.xss.interceptor.XssCleanInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * xss自动配置类
 *
 * @author luyanan
 * @since 2021/8/18
 **/
@RequiredArgsConstructor
@ConditionalOnProperty(value = XssProperties.PREFIX + ".enable", havingValue = "true", matchIfMissing = false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(XssProperties.class)
public class XssAutoConfiguration implements WebMvcConfigurer {

	private final XssProperties xssProperties;

	/**
	* formXssClean
	* @since 2022/3/3
	* @return com.rainsoil.common.framework.xss.core.FormXssClean
	*/
	@Bean
	public FormXssClean formXssClean() {
		return new FormXssClean();
	}

	/**
	* xssJacksonCustomizer
	* @since 2022/3/3
	* @return org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
	*/
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer() {

		return builder -> builder.deserializerByType(String.class, new JacksonXssClean());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		XssCleanInterceptor xssCleanInterceptor = new XssCleanInterceptor(xssProperties);
		registry.addInterceptor(xssCleanInterceptor).addPathPatterns(xssProperties.getPathPatterns())
				.excludePathPatterns(xssProperties.getExcludePatterns()).order(Ordered.LOWEST_PRECEDENCE);
	}

}
