package com.rainsoil.common.framework.threadpool.endpoint;

import com.rainsoil.common.framework.threadpool.DynamicThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态线程池端点暴漏
 *
 * @author luyanan
 * @since 2021/8/20
 **/
@Endpoint(id = "dynamic-thread-pool")
public class DynamicThreadPoolEndpoint {

	@Autowired
	private DynamicThreadPoolManager dynamicThreadPoolManager;

	/**
	 * 线程池端点
	 *
	 * @return java.util.Map<java.lang.String, java.lang.Object>
	 * @since 2022/3/3
	 */
	@ReadOperation
	public Map<String, Object> threadPools() {
		Map<String, Object> data = new HashMap<>(1);
		List<Map<String, Object>> monitor = dynamicThreadPoolManager.monitor();

		data.putIfAbsent("threadPools", monitor);
		return data;
	}

}
