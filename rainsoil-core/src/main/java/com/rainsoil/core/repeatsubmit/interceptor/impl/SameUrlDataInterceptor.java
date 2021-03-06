package com.rainsoil.core.repeatsubmit.interceptor.impl;

import cn.hutool.json.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainsoil.core.repeatsubmit.interceptor.AbstractRepeatSubmitInterceptor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 判断请求url和数据是否和上一次相同，
 * 如果和上次相同，则是重复提交表单。 有效时间为10秒内。
 *
 * @author ruoyi
 */
@Component
public class SameUrlDataInterceptor extends AbstractRepeatSubmitInterceptor {
	public static final  String REPEAT_PARAMS = "repeatParams";

	public static final String REPEAT_TIME = "repeatTime";

	public static final String SESSION_REPEAT_KEY = "repeatData";

	/**
	 * 间隔时间，单位:秒 默认10秒
	 * <p>
	 * 两次相同参数的请求，如果间隔时间大于该参数，系统不会认定为重复提交的数据
	 */
	private int intervalTime = 10;

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isRepeatSubmit(HttpServletRequest request) throws Exception {
		// 本次参数及系统时间
		String nowParams = new ObjectMapper().writeValueAsString(request.getParameterMap());
		Map<String, Object> nowDataMap = new HashMap<String, Object>(8);
		nowDataMap.put(REPEAT_PARAMS, nowParams);
		nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

		// 请求地址（作为存放session的key值）
		String url = request.getRequestURI();

		HttpSession session = request.getSession();
		Object sessionObj = session.getAttribute(SESSION_REPEAT_KEY);
		if (sessionObj != null) {
			Map<String, Object> sessionMap = (Map<String, Object>) sessionObj;
			if (sessionMap.containsKey(url)) {
				Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
				if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap)) {
					return true;
				}
			}
		}
		Map<String, Object> sessionMap = new HashMap<String, Object>(1);
		sessionMap.put(url, nowDataMap);
		session.setAttribute(SESSION_REPEAT_KEY, sessionMap);
		return false;
	}

	/**
	 * 判断参数是否相同
	 */
	private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
		String nowParams = (String) nowMap.get(REPEAT_PARAMS);
		String preParams = (String) preMap.get(REPEAT_PARAMS);
		return nowParams.equals(preParams);
	}

	/**
	 * 1000
	 *
	 * @since 2022/2/10
	 */

	private static final int THOUSAND = 1000;

	/**
	 * 判断两次间隔时间
	 */
	private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap) {
		long time1 = (Long) nowMap.get(REPEAT_TIME);
		long time2 = (Long) preMap.get(REPEAT_TIME);
		if ((time1 - time2) < (this.intervalTime * THOUSAND)) {
			return true;
		}
		return false;
	}
}
