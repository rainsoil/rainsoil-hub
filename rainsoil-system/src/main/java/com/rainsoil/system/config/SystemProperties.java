package com.rainsoil.system.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 *
 * @author luyanan
 * @since 2022/2/20
 **/
@Data
@Component
@ConfigurationProperties(prefix = SystemProperties.PREFIX)
public class SystemProperties {
	/**
	 * 前缀
	 *
	 * @since 2022/2/20
	 */

	public static final String PREFIX = "system";


	/**
	 * CopyrightYear
	 *
	 * @since 2022/2/22
	 */
	@ApiModelProperty(value = "CopyrightYear")
	private String copyrightYear;

	/**
	 * 版本
	 *
	 * @since 2022/2/22
	 */
	@ApiModelProperty(value = "版本")
	private String version;
}
