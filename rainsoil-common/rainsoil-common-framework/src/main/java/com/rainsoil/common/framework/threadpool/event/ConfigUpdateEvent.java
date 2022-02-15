package com.rainsoil.common.framework.threadpool.event;

import org.springframework.context.ApplicationEvent;

/**
 * 配置变化事件
 *
 * @author luyanan
 * @since 2021/8/23
 **/
public class ConfigUpdateEvent extends ApplicationEvent {

	public ConfigUpdateEvent(Object source) {
		super(source);
	}

}
