package com.rainsoil.common.security.security.config;

import com.rainsoil.common.core.constants.PropertiesConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * 安全模块配置
 *
 * @author luyanan
 * @since 2021/10/5
 **/
@Data
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties {

	public static final String PREFIX = PropertiesConstants.PERFIX + ".spring.security";

	/**
	 * 忽略登录的url
	 *
	 * @since 2021/10/5
	 */

	private Set<String> ignoringLoginUrl;

	/**
	 * jwt的相关配置
	 *
	 * @since 2021/10/7
	 */

	private JwtProperties jwt = new JwtProperties();

}
