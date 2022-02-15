package com.rainsoil.common.framework.threadpool.interceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态线程拦截器抽象类
 *
 * @author luyanan
 * @since 2021/8/20
 **/
public abstract class AbstractDynamicThreadInterceptor implements DynamicThreadInterceptor {

	@Override
	public void main(ThreadLocal<Map<String, Object>> threadLocal) {
		Map<String, Object> map = threadLocal.get();
		if (null == map) {
			map = new ConcurrentHashMap<>(16);
		}
		mainHandler(map);
		threadLocal.set(map);
	}

	@Override
	public void childThread(ThreadLocal<Map<String, Object>> threadLocal) {
		Map<String, Object> map = threadLocal.get();
		childThreadHandler(map);
	}

	/**
	 * 主线程执行的方法
	 * @param attrMap
	 * @return void
	 * @since 2021/8/20
	 */
	protected abstract void mainHandler(Map<String, Object> attrMap);

	/**
	 * 子线程执行的方法
	 * @param attrMap
	 * @return void
	 * @since 2021/8/20
	 */
	protected abstract void childThreadHandler(Map<String, Object> attrMap);

}
