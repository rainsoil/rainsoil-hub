package com.rainsoil.common.framework.spring;

import com.rainsoil.common.framework.spring.cors.CorsConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * spring自动配置
 *
 * @author luyanan
 * @since 2021/8/22
 **/
@Configuration
@EnableConfigurationProperties(SpringProperties.class)
@Import({ CorsConfig.class, SpringContextHolder.class })
public class SpringAutoConfiguration {

}
