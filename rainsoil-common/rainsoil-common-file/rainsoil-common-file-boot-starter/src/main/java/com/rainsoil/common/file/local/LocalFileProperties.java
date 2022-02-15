package com.rainsoil.common.file.local;

import com.rainsoil.common.file.config.FileProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 本地文件配置
 *
 * @author luyanan
 * @since 2022/2/7
 **/
@Data
@ConfigurationProperties(LocalFileProperties.PREFIX)
public class LocalFileProperties {


	public static final String PREFIX = FileProperties.PREFIX + ".local";


	/**
	 * 数据存放目录
	 *
	 * @since 2022/2/7
	 */
	private String data = "/data";

}
