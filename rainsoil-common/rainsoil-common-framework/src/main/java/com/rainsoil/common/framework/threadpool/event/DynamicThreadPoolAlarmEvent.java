package com.rainsoil.common.framework.threadpool.event;

import org.springframework.context.ApplicationEvent;

/**
 * 动态线程池告警
 *
 * @author luyanan
 * @since 2021/8/24
 **/
public class DynamicThreadPoolAlarmEvent extends ApplicationEvent {

	public DynamicThreadPoolAlarmEvent(Object source) {
		super(source);
	}

}
