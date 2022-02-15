package com.rainsoil.common.framework.threadpool.listener;// package com.rainsoil.fastjava.common.framework.threadpool.listener;
//
// import com.alibaba.cloud.nacos.NacosConfigProperties;
// import com.alibaba.nacos.api.config.ConfigService;
// import com.alibaba.nacos.api.config.listener.AbstractListener;
// import com.alibaba.nacos.api.exception.NacosException;
// import com.rainsoil.fastjava.common.framework.spring.SpringContextHolder;
// import com.rainsoil.fastjava.common.framework.threadpool.DynamicThreadPoolManager;
// import
// com.rainsoil.fastjava.common.framework.threadpool.config.DynamicThreadPoolProperties;
// import com.rainsoil.fastjava.common.framework.threadpool.event.ConfigUpdateEvent;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.util.Assert;
//
// import javax.annotation.PostConstruct;
//
/// **
// * Spring Cloud Alibaba Nacos的配置修改监听
// *
// * @author luyanan
// * @since 2021/8/23
// **/
// @Slf4j
// public class NacosCloudConfigUpdateListener {
// @Autowired
// private NacosConfigProperties nacosConfigProperties;
//
// @Autowired
// private DynamicThreadPoolManager dynamicThreadPoolManager;
//
// @Autowired
// private DynamicThreadPoolProperties poolProperties;
//
//
// @PostConstruct
// public void init() {
// initConfigUpdateListener();
// }
//
// public void initConfigUpdateListener() {
// ConfigService configService = nacosConfigProperties.configServiceInstance();
// Assert.hasText(poolProperties.getNacosDataId(), "请配置nacosDataId");
// Assert.hasText(poolProperties.getNacosGroup(), "请配置nacosGroup");
//
// try {
// configService.addListener(poolProperties.getNacosDataId(),
// poolProperties.getNacosGroup(), new AbstractListener() {
// @Override
// public void receiveConfigInfo(String configInfo) {
// SpringContextHolder.publishEvent(new ConfigUpdateEvent(configInfo));
//// new Thread(() -> dynamicThreadPoolManager.refreshThreadPoolExecutor(true)).start();
// log.info("线程池配置有变化，刷新完成");
// }
// });
// } catch (NacosException e) {
// log.error("Nacos配置监听异常", e);
// }
// }
//
// }