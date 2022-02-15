package com.rainsoil.common.data.logger;

import cn.hutool.json.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainsoil.common.data.logger.config.LoggerProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * web打印事件监听
 *
 * @author luyanan
 * @since 2021/8/24
 **/
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = LoggerProperties.PREFIX + ".logger.print", havingValue = "true", matchIfMissing = true)
public class WebPrintLoggerEventListener extends AbstractLoggerParserHandler
		implements ApplicationListener<LoggerEvent> {

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * 方法名
	 *
	 * @since 2022/2/9
	 */

	public static final String METHOD_NAME = "methodName";


	/**
	 * 分隔符
	 *
	 * @since 2022/2/9
	 */

	public static final String SEPARATOR = ":";


	/**
	 * 来源ip
	 *
	 * @since 2022/2/9
	 */

	public static final String SOURCE_IP = "sourceIp";


	/**
	 * 简介
	 *
	 * @since 2022/2/9
	 */

	public static final String DESP = "desp";


	/**
	 * 请求参数
	 *
	 * @since 2022/2/9
	 */

	public static final String REQUEST_PARAMS = "requestParams";

	/**
	 * 换行
	 *
	 * @since 2022/2/9
	 */

	public static final String LINE_FEED = "\n";

	/**
	 * 开始时间
	 *
	 * @since 2022/2/9
	 */

	public static final String START_TIME = "startTime";


	/**
	 * uri
	 *
	 * @since 2022/2/9
	 */

	public static final String URI = "uri";

	/**
	 * endTime
	 *
	 * @since 2022/2/9
	 */

	public static final String END_TIME = "endTime";

	/**
	 * 耗时
	 *
	 * @since 2022/2/9
	 */

	public static final String TIME_CONSUMING = "timeConsuming";


	/**
	 * 结果
	 *
	 * @since 2022/2/9
	 */

	public static final String RESULT = "result";


	/**
	 * 错误信息
	 *
	 * @since 2022/2/9
	 */

	public static final String ERROR_MSG = "errorMsg";

	@SneakyThrows
	@Override
	public void onApplicationEvent(LoggerEvent loggerEvent) {

		if (null == loggerEvent || !(loggerEvent.getSource() instanceof LoggerEventDto)) {
			return;
		}

		LoggerEventDto loggerEventDto =
				(LoggerEventDto) loggerEvent.getSource();

		Map<String, Object> map = parser(loggerEventDto);

		StringBuffer sb = new StringBuffer();
		sb.append(LINE_FEED + "[-----------------------------------")
				.append(LINE_FEED)
				.append(METHOD_NAME + SEPARATOR)
				.append(map.get(METHOD_NAME))
				.append(LINE_FEED)
				.append(SOURCE_IP + SEPARATOR)
				.append(map.get(SOURCE_IP))
				.append(LINE_FEED)
				.append(DESP + SEPARATOR)
				.append(map.get(DESP))
				.append(LINE_FEED)
				.append(URI + SEPARATOR)
				.append(map.get(URI))
				.append(LINE_FEED);
		if (null != map.get(REQUEST_PARAMS)) {


			sb.append(REQUEST_PARAMS + SEPARATOR)
					.append(objectMapper.writeValueAsString(map.get(REQUEST_PARAMS))).append(LINE_FEED);
		}
		// 耗时
		if (null != map.get(START_TIME) && null != map.get(END_TIME)) {
			sb.append(TIME_CONSUMING + SEPARATOR).append(((Long) map.get(END_TIME)) - ((Long) map.get(START_TIME)))
					.append(LINE_FEED);
		}
		if (null != map.get(RESULT)) {
			sb.append(RESULT + SEPARATOR).append(objectMapper.writeValueAsString(map.get(RESULT))).append(LINE_FEED);
		}
		if (null != map.get(ERROR_MSG)) {
			sb.append(ERROR_MSG + SEPARATOR)
					.append(objectMapper.writeValueAsString(map.get(ERROR_MSG))).append(LINE_FEED);
		}
		sb.append("------------------------------]");
		log.debug(sb.toString());
	}

}
