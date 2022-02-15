package com.rainsoil.common.security.validatecode.config;

import com.rainsoil.common.security.security.config.SecurityProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 验证码的配置文件
 *
 * @author luyanan
 * @since 2021/10/5
 **/
@Data
@ConfigurationProperties(prefix = ValidateCodeProperties.PREFIX)
public class ValidateCodeProperties {

	public static final String PREFIX = SecurityProperties.PREFIX + ".validatecode";

	/**
	 * 是否开启
	 *
	 * @since 2021/10/5
	 */

	private Boolean enable;

	/**
	 * 验证码的类型
	 *
	 * @since 2021/10/5
	 */

	private String captchaType;

	/**
	 * 是否开启验证码url过滤
	 *
	 * @since 2021/10/5
	 */

	private Boolean validateCodeFilter;

	/**
	 * 验证码过滤的url
	 *
	 * @since 2021/10/5
	 */

	private Set<String> validateCodeUrls = new HashSet<>();

	/**
	 * 验证码的过滤的key
	 *
	 * @since 2021/10/5
	 */

	private String validateCodeFilterKeyParameter = "uuid";

	/**
	 * 验证码过滤的code
	 *
	 * @since 2021/10/5
	 */

	private String validateCodeFilterCodeParameter = "code";

}
