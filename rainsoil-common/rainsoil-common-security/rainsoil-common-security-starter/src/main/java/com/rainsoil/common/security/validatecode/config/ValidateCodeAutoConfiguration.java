package com.rainsoil.common.security.validatecode.config;

import com.rainsoil.common.security.core.validatecode.storage.ValidateCodeStorage;
import com.rainsoil.common.security.validatecode.ValidateCodeTemplate;
import com.rainsoil.common.security.validatecode.storage.CacheManageValidateCodeStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码自动配置类
 *
 * @author luyanan
 * @since 2021/10/6
 **/
@ConditionalOnProperty(prefix = ValidateCodeProperties.PREFIX, value = "enable", havingValue = "true",
		matchIfMissing = false)
@EnableConfigurationProperties(ValidateCodeProperties.class)
@Configuration
public class ValidateCodeAutoConfiguration {

	/**
	 * 验证码存储
	 *
	 * @return com.rainsoil.common.security.core.validatecode.storage.ValidateCodeStorage
	 * @since 2022/3/6
	 */
	@Bean
	@ConditionalOnBean(CacheManager.class)
	public ValidateCodeStorage validateCodeStorage() {
		return new CacheManageValidateCodeStorage();
	}

	/**
	 * 验证码生成
	 *
	 * @return com.rainsoil.common.security.validatecode.ValidateCodeTemplate
	 * @since 2022/3/6
	 */
	@Bean
	public ValidateCodeTemplate validateCodeTemplate() {
		return new ValidateCodeTemplate();
	}

}
