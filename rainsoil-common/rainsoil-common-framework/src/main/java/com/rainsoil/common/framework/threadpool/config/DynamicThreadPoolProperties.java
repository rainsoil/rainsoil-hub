package com.rainsoil.common.framework.threadpool.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 动态线程池配置
 *
 * @author luyanan
 * @since 2021/8/19
 **/

@Slf4j
@Data
@ConfigurationProperties(DynamicThreadPoolProperties.PREFIX)
public class DynamicThreadPoolProperties {

	public static final String PREFIX = "dynamic.threadpool";

	/**
	 * 告警的配置
	 *
	 * @since 2021/8/24
	 */

	private AlarmProperties alarm = new AlarmProperties();

	/**
	 * Nacos DataId, 监听配置修改用
	 */
	private String nacosDataId;

	/**
	 * Nacos Group, 监听配置修改用
	 */
	private String nacosGroup;

	/**
	 * Nacos 等待配置刷新时间间隔（监听器收到消息变更通知，此时Spring容器中的配置bean还没更新，需要等待固定的时间）
	 */
	private int nacosWaitRefreshConfigSeconds = 1;

	/**
	 * Apollo的namespace, 监听配置修改用
	 */
	private String apolloNamespace;

	/**
	 * 告警时间间隔，单位分钟
	 */
	private int alarmTimeInterval = 1;

	/**
	 * 等待配置刷新时间间隔（监听器收到消息变更通知，此时Spring容器中的配置bean还没更新，需要等待固定的时间）
	 *
	 * @since 2021/8/20
	 */

	private int waitRefreshConfigSeconds = 1;

	/**
	 * 告警阈值百分比
	 *
	 * @since 2021/8/23
	 */

	private int queueCapacityThreshold = 80;

	/**
	 * 线程池配置
	 *
	 * @since 2021/8/19
	 */

	private List<ThreadPoolProperties> executors = new ArrayList<>();

	/**
	 * 刷新配置文件
	 *
	 * @param content 整个配置文件的内容
	 * @since 2021/8/20
	 */
	@SneakyThrows
	public void refresh(String content) {
		Properties properties = new Properties();
		properties.load(new ByteArrayInputStream(content.getBytes(Charset.forName("utf-8"))));

		doRefresh(properties);
	}

	/**
	 * 刷新yml
	 *
	 * @param content 内容
	 * @since 2022/3/3
	 */
	@SneakyThrows
	public void refreshYaml(String content) {
		if (StrUtil.isBlank(content)) {
			return;
		}
		YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
		yamlPropertiesFactoryBean.setResources(new ByteArrayResource(content.getBytes("utf-8")));
		Properties properties = yamlPropertiesFactoryBean.getObject();
		if (null != properties) {
			doRefresh(properties);
		}
	}

	/**
	 * 执行刷新操作
	 *
	 * @param properties 配置
	 * @since 2022/3/3
	 */
	private void doRefresh(Properties properties) {

		HashMap<String, String> dataMap = new HashMap<String, String>((Map) properties);
		ConfigurationPropertySource source = new MapConfigurationPropertySource(dataMap);
		Binder binder = new Binder(source);
		binder.bind(DynamicThreadPoolProperties.PREFIX, Bindable.ofInstance(this)).get();

	}

}
