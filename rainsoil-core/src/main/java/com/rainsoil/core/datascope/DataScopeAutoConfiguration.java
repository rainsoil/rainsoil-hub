package com.rainsoil.core.datascope;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据权限自动化配置
 *
 * @author luyanan
 * @since 2022/2/23
 **/
@ConditionalOnBean(IDataScopeFilter.class)
@Configuration
public class DataScopeAutoConfiguration {

	@Bean
	public DataScopeAspect dataScopeAspect() {
		return new DataScopeAspect();
	}

}
