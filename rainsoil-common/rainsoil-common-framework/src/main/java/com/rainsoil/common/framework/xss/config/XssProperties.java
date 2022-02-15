package com.rainsoil.common.framework.xss.config;

import cn.hutool.core.collection.CollUtil;
import com.rainsoil.common.core.constants.PropertiesConstants;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * xss的配置文件
 *
 * @author luyanan
 * @since 2021/8/17
 **/
@Data
@ConfigurationProperties(XssProperties.PREFIX)
public class XssProperties implements InitializingBean {

	public static final String PREFIX = PropertiesConstants.PERFIX + ".xss";

	/**
	 * 开启xss
	 *
	 * @since 2021/8/17
	 */

	private boolean enable = true;

	/**
	 * 拦截的路由，默认拦截 /**
	 *
	 * @since 2021/8/17
	 */

	private List<String> pathPatterns = new ArrayList<>();

	/**
	 * 放行的规则，默认为空
	 *
	 * @since 2021/8/17
	 */

	private List<String> excludePatterns = new ArrayList<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		if (CollUtil.isEmpty(pathPatterns)) {
			pathPatterns.add("/**");
		}
	}

}
