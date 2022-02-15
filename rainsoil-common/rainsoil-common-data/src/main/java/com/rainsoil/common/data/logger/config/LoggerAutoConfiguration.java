package com.rainsoil.common.data.logger.config;

import com.rainsoil.common.data.logger.LoggerAspect;
import com.rainsoil.common.data.logger.WebPrintLoggerEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 日志自动配置类
 *
 * @author luyanan
 * @since 2021/8/24
 **/
@Slf4j
@EnableConfigurationProperties(LoggerProperties.class)
@Configuration
@ConditionalOnProperty(value = LoggerProperties.PREFIX + ".enable", havingValue = "true", matchIfMissing = true)
@Import(WebPrintLoggerEventListener.class)

public class LoggerAutoConfiguration {

	@Bean
	public LoggerAspect loggerAspect() {
		log.debug("开启日志功能");
		return new LoggerAspect();
	}

}
