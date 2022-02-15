package com.rainsoil.common.data.logger.config;

import com.rainsoil.common.core.constants.PropertiesConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志配置文件
 *
 * @author luyanan
 * @since 2021/8/24
 **/
@Data
@ConfigurationProperties(prefix = LoggerProperties.PREFIX)
public class LoggerProperties {

	public static final String PREFIX = PropertiesConstants.PERFIX + ".logger";

	/**
	 * 是否开启, 默认不开启
	 *
	 * @since 2021/8/24
	 */
	private boolean enable = false;

	/**
	 * 是否开启打印
	 *
	 * @since 2021/8/24
	 */

	private boolean print = false;

}
