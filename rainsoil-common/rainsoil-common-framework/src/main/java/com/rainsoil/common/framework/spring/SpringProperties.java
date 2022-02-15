package com.rainsoil.common.framework.spring;

import com.rainsoil.common.core.constants.PropertiesConstants;
import com.rainsoil.common.framework.spring.cors.CorsProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * spring的配置文件
 *
 * @author luyanan
 * @since 2021/8/22
 **/
@Data
@ConfigurationProperties(prefix = SpringProperties.PREFIX)
public class SpringProperties {

	/**
	 * 前缀
	 *
	 * @since 2022/2/8
	 */

	public static final String PREFIX = PropertiesConstants.PERFIX + ".spring";

	/**
	 * cors的配置
	 *
	 * @since 2021/8/22
	 */

	private CorsProperties cors = new CorsProperties();

}
