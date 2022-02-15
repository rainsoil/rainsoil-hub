package com.rainsoil.common.data.logger;

import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 *
 * @author luyanan
 * @since 2021/8/22
 **/
public class LoggerEvent extends ApplicationEvent {

	/**
	 * Create a new {@code ApplicationEvent}.
	 * @param source the object on which the event initially occurred or with which the
	 * event is associated (never {@code null})
	 */
	public LoggerEvent(Object source) {
		super(source);
	}

}
