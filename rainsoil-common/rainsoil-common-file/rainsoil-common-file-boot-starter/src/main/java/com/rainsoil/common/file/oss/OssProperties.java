package com.rainsoil.common.file.oss;

import com.rainsoil.common.file.config.FileProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * aws 配置信息
 * oss: enable: true
 * pathStyleAccess 采用nginx反向代理或者AWS S3 配置成true，支持第三方云存储配置成false pathStyleAccess: false
 * bucket 设置公共读权限
 *
 * @author luyanan
 * @since 2021/10/23
 **/
@Data
@ConfigurationProperties(prefix = OssProperties.PREFIX)
public class OssProperties {


	public static final String PREFIX = FileProperties.PREFIX + ".oss";

	/**
	 * 是否开启
	 *
	 * @since 2021/10/24
	 */

	private Boolean enable;

	/**
	 * 对象存储服务的URL
	 */
	private String endpoint;


	/**
	 * 自定义域名
	 */
	private String customDomain;

	/**
	 * true path-style nginx 反向代理和S3默认支持 pathStyle {http://endpoint/bucketname} false
	 * supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style
	 * 模式{http://bucketname.endpoint}
	 */
	private Boolean pathStyleAccess = true;


	/**
	 * 应用ID
	 */
	private String appId;


	/**
	 * 区域
	 */
	private String region;


	/**
	 * Access key就像用户ID，可以唯一标识你的账户
	 */
	private String accessKey;

	/**
	 * Secret key是你账户的密码
	 */
	private String secretKey;


	/**
	 * 默认的存储桶名称
	 */
	private String bucketName;


	/**
	 * 最大线程数，默认： 100
	 */
	private Integer maxConnections = 100;
}
