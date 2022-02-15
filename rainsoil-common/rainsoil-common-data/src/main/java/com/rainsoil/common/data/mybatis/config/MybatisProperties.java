package com.rainsoil.common.data.mybatis.config;

import com.rainsoil.common.core.constants.PropertiesConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mybatis的配置
 *
 * @author luyanan
 * @since 2022/2/9
 **/
@Data
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
public class MybatisProperties {

	public static final String PREFIX = PropertiesConstants.PERFIX + ".mybatis";

	/**
	 * 是否打印可执行 sql
	 */
	private boolean showSql = true;
}
