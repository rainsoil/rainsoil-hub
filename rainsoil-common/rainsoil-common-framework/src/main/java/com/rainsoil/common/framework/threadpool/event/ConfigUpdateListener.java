package com.rainsoil.common.framework.threadpool.event;

import com.rainsoil.common.framework.threadpool.DynamicThreadPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

/**
 * 配置发现变化的事件监听
 *
 * @author luyanan
 * @since 2021/8/23
 **/
@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
@Slf4j
public class ConfigUpdateListener implements ApplicationListener<ConfigUpdateEvent> {

	@Autowired
	private DynamicThreadPoolManager dynamicThreadPoolManager;

	@Override
	public void onApplicationEvent(ConfigUpdateEvent configUpdateEvent) {
		Object source = configUpdateEvent.getSource();
		if (null != source) {
			// noinspection AlibabaAvoidManuallyCreateThread
			new Thread(() -> dynamicThreadPoolManager.refreshThreadPoolExecutor(true)).start();
			log.info("线程池配置有变化，刷新完成");
		}

	}

}
