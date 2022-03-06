package com.rainsoil.common.data.logger;

import cn.hutool.http.server.HttpServerRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.Signature;

import java.io.Serializable;

/**
 * 日志事件传输对象
 *
 * @author luyanan
 * @since 2021/8/24
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoggerEventDto implements Serializable {

	/**
	 * 返回结果
	 *
	 * @since 2021/8/22
	 */

	private Object result;

	/**
	 * 开始时间
	 *
	 * @since 2021/8/22
	 */

	private Long startTime;

	/**
	 * 结束时间
	 *
	 * @since 2021/8/24
	 */

	private Long endTime;

	/**
	 * 异常
	 *
	 * @since 2021/8/22
	 */

	private Throwable ex;

	private Signature signature;

	/**
	 * 参数
	 *
	 * @since 2021/8/24
	 */

	@SuppressWarnings(value = "all")
	private Object[] args;

	/**
	 * 请求
	 *
	 * @since 2021/8/24
	 */

	private HttpServerRequest request;

}
