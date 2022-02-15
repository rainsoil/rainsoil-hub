//package com.rainsoil.common.framework.threadpool.listener;
//
//import com.ctrip.framework.apollo.Config;
//import com.ctrip.framework.apollo.ConfigFile;
//import com.ctrip.framework.apollo.ConfigService;
//import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
//
//import com.rainsoil.common.framework.spring.SpringContextHolder;
//import com.rainsoil.common.framework.threadpool.config.DynamicThreadPoolProperties;
//import com.rainsoil.common.framework.threadpool.event.ConfigUpdateEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.PostConstruct;
//
///**
// * Apollo配置修改监听
// *
// * @author luyanan
// * @since 2021/8/23
// **/
//@Slf4j
//public class ApolloConfigUpdateListener {
//
//	@Value("${apollo.bootstrap.namespaces:application}")
//	private String namespace;
//
//	@Autowired
//	private DynamicThreadPoolProperties poolProperties;
//
//	@PostConstruct
//	public void init() {
//		initConfigUpdateListener();
//	}
//
//	public void initConfigUpdateListener() {
//		String apolloNamespace = namespace;
//		if (StringUtils.hasText(poolProperties.getApolloNamespace())) {
//			apolloNamespace = poolProperties.getApolloNamespace();
//		}
//
//		String finalApolloNamespace = apolloNamespace;
//
//		Config config = ConfigService.getConfig(finalApolloNamespace);
//
//		config.addChangeListener(changeEvent -> {
//			ConfigFileFormat configFileFormat = ConfigFileFormat.Properties;
//			String getConfigNamespace = finalApolloNamespace;
//			if (finalApolloNamespace.contains(ConfigFileFormat.YAML.getValue())) {
//				configFileFormat = ConfigFileFormat.YAML;
//				// 去除.yaml后缀，getConfigFile时候会根据类型自动追加
//				getConfigNamespace = getConfigNamespace.replaceAll("." + ConfigFileFormat.YAML.getValue(), "");
//			}
//
//			ConfigFile configFile = ConfigService.getConfigFile(getConfigNamespace, configFileFormat);
//			String content = configFile.getContent();
//
//			if (finalApolloNamespace.contains(ConfigFileFormat.YAML.getValue())) {
//				poolProperties.refreshYaml(content);
//			}
//			else {
//				poolProperties.refresh(content);
//			}
//
//			SpringContextHolder.publishEvent(new ConfigUpdateEvent(content));
//			log.info("线程池配置有变化，刷新完成");
//		});
//	}
//
//}