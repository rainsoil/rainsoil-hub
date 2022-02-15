package com.rainsoil.common.file.config;

import com.rainsoil.common.core.constants.PropertiesConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件配置
 *
 * @author luyanan
 * @since 2022/2/6
 **/
@Data
@ConfigurationProperties(prefix = FileProperties.PREFIX)
public class FileProperties {

	public final static String PREFIX = PropertiesConstants.PERFIX + ".file";
	/**
	 * 是否开启
	 *
	 * @since 2022/2/6
	 */
	private boolean enable;

	/**
	 * 文件的domain
	 *
	 * @since 2022/2/6
	 */
	private String domain;


	/**
	 * 文件服务实现类型
	 *
	 * @since 2022/2/7
	 */

	private String type;
}
