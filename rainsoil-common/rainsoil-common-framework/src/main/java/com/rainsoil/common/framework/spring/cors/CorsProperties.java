package com.rainsoil.common.framework.spring.cors;

import com.rainsoil.common.framework.spring.SpringProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * cors配置文件
 *
 * @author luyanan
 * @since 2021/8/22
 **/
@ConfigurationProperties(prefix = CorsProperties.PREFIX)
@Data
public class CorsProperties {
	/**
	 * 前缀
	 *
	 * @since 2022/2/8
	 */

	public static final String PREFIX = SpringProperties.PREFIX + ".cors";

	/**
	 * 是否启动
	 *
	 * @since 2021/2/10
	 */
	private Boolean enable = false;

	/**
	 * 允许跨域的路径
	 *
	 * @since 2021/2/10
	 */
	private String pathPattern = "/**";

	/**
	 * 允许跨域的域名,多个用,分割
	 *
	 * @since 2021/2/10
	 */
	private String origins = "*";

	/**
	 * 允许跨域的请求方法,多个用,分割
	 *
	 * @since 2021/2/10
	 */
	private String methods = "GET,POST,DELETE,PUT,PATCH";

	/**
	 * 允许跨域的header,多个用,分割
	 *
	 * @since 2021/2/10
	 */
	private String headers = "*";

	/**
	 * 是否允许携带cookie信息
	 *
	 * @since 2021/2/10
	 */
	private boolean allowCredentials = true;

	/**
	 * maxAge表明在maxAge秒内，不需要再发送预检验请求，可以缓存该结果
	 *
	 * @since 2021/2/10
	 */
	private long maxAge = 3600;

	/**
	 * 排除的请求头
	 *
	 * @since 2021/2/10
	 */
	private String exposedHeaders;

}
