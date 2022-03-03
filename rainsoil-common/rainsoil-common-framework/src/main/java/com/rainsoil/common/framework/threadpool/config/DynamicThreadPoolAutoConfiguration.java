package com.rainsoil.common.framework.threadpool.config;

import com.rainsoil.common.framework.threadpool.DynamicThreadPoolManager;
import com.rainsoil.common.framework.threadpool.alarm.DynamicThreadPoolAlarm;
import com.rainsoil.common.framework.threadpool.endpoint.DynamicThreadPoolEndpoint;
import com.rainsoil.common.framework.threadpool.event.ConfigUpdateListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态线程池自动配置类
 *
 * @author luyanan
 * @since 2021/8/24
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicThreadPoolProperties.class)
public class DynamicThreadPoolAutoConfiguration {

	/**
	 * 动态线程管理器
	 *
	 * @return com.rainsoil.common.framework.threadpool.DynamicThreadPoolManager
	 * @since 2022/3/3
	 */
	@Bean
	public DynamicThreadPoolManager dynamicThreadPoolManager() {
		log.debug("开启DynamicThreadPoolManager");
		return new DynamicThreadPoolManager();
	}

	/**
	 * 动态线程端点
	 *
	 * @return com.rainsoil.common.framework.threadpool.endpoint.DynamicThreadPoolEndpoint
	 * @since 2022/3/3
	 */
	@Bean
	public DynamicThreadPoolEndpoint dynamicThreadPoolEndpoint() {
		return new DynamicThreadPoolEndpoint();
	}

	/**
	 * 线程池报警
	 *
	 * @return com.rainsoil.common.framework.threadpool.alarm.DynamicThreadPoolAlarm
	 * @since 2022/3/3
	 */
	@Bean
	@ConditionalOnProperty(value = DynamicThreadPoolProperties.PREFIX + ".alarm.enabled", havingValue = "true",
			matchIfMissing = false)
	public DynamicThreadPoolAlarm dynamicThreadPoolAlarm() {
		return new DynamicThreadPoolAlarm();
	}

	/**
	 * 配置刷新监听
	 *
	 * @return com.rainsoil.common.framework.threadpool.event.ConfigUpdateListener
	 * @since 2022/3/3
	 */
	public ConfigUpdateListener configUpdateListener() {
		return new ConfigUpdateListener();
	}

//	@Configuration
//	@ConditionalOnClass(value = com.ctrip.framework.apollo.ConfigService.class)
//	protected static class ApolloConfiguration {
//
//		@Bean
//		public ApolloConfigUpdateListener apolloConfigUpdateListener() {
//			return new ApolloConfigUpdateListener();
//		}
//
//	}

}
