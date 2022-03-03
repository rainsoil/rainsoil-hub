package com.rainsoil.common.framework.threadpool;


import com.rainsoil.common.framework.threadpool.interceptor.DynamicThreadInterceptor;
import org.springframework.beans.factory.ObjectProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 带有拦截器的线程
 *
 * @author luyanan
 * @since 2021/8/20
 **/
public class AsyncRunnable implements Runnable {

	private Runnable runnable;

	private Map<String, Object> map = new ConcurrentHashMap<>();

	private ThreadLocal<Map<String, Object>> threadLocal;

	private ObjectProvider<DynamicThreadInterceptor> threadInterceptors;

	public AsyncRunnable(Runnable runnable, ThreadLocal<Map<String, Object>> threadLocal,
						 ObjectProvider<DynamicThreadInterceptor> threadInterceptors) {
		this.runnable = runnable;
		this.threadLocal = threadLocal;
		this.threadInterceptors = threadInterceptors;
		copy(runnable, threadLocal);
	}

	@Override
	public void run() {
		try {
			threadLocal.set(map);
			threadInterceptors.orderedStream().forEachOrdered(a -> {
				a.childThread(threadLocal);
			});
			this.runnable.run();
		} finally {

			threadLocal.remove();
			this.map = null;
		}
	}

	/**
	 * copy
	 *
	 * @param runnable    线程
	 * @param threadLocal threadLocal
	 * @since 2022/3/3
	 */
	private void copy(Runnable runnable, ThreadLocal<Map<String, Object>> threadLocal) {
		if (null != threadLocal.get()) {
			map.putAll(threadLocal.get());
		}
	}

}
