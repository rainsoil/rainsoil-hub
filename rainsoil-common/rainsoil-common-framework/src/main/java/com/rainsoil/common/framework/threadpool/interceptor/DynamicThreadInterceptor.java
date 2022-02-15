package com.rainsoil.common.framework.threadpool.interceptor;

import java.util.Map;

/**
 * 动态线程拦截器
 *
 * @author luyanan
 * @since 2021/8/20
 **/
public interface DynamicThreadInterceptor {

	/**
	 * 主线程执行的方法
	 * @param threadLocal
	 * @return void
	 * @since 2021/8/20
	 */
	void main(ThreadLocal<Map<String, Object>> threadLocal);

	/**
	 * 线程池执行的方法
	 * @param threadLocal
	 * @return void
	 * @since 2021/8/20
	 */
	void childThread(ThreadLocal<Map<String, Object>> threadLocal);

}
