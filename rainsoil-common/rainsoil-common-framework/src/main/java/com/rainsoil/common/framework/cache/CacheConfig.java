package com.rainsoil.common.framework.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Array;

/**
 * 缓存配置
 *
 * @author luyanan
 * @since 2021/9/18
 **/
@AutoConfigureAfter(CacheManager.class)
@Slf4j
public class CacheConfig extends CachingConfigurerSupport {

	/**
	 * 没有参数的占位
	 *
	 * @since 2021/2/9
	 */
	public static final int NO_PARAM_KEY = 0;

	/**
	 * 当参数为NULL的占位
	 *
	 * @since 2021/2/9
	 */
	public static final int NULL_PARAM_KEY = 53;

	@Autowired
	private CacheManager cacheManager;

	/**
	 * 生成key的策略 此方法将会根据类名+方法名+所有参数的值生成唯一的一个key,即使@Cacheable中的value属性一样，key也会不一样。
	 *
	 * @return
	 */
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return (target, method, params) -> {
			StringBuilder key = new StringBuilder();
			key.append(target.getClass().getName()).append(":");
			key.append(method.getName()).append(":");
			if (params.length == 0) {
				return key.append(NO_PARAM_KEY).toString();
			}
			for (Object param : params) {
				if (param == null) {
					key.append(NULL_PARAM_KEY);
				} else if (ClassUtils.isPrimitiveArray(param.getClass())) {
					int length = Array.getLength(param);
					for (int i = 0; i < length; i++) {
						key.append(Array.get(param, i));
						key.append(',');
					}
				} else if (ClassUtils.isPrimitiveOrWrapper(param.getClass()) || param instanceof String) {
					key.append(param);
				} else {
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						key.append(objectMapper.writeValueAsString(param));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}

			}
			return key.toString();

		};
	}

	@Override
	public CacheResolver cacheResolver() {
		return new ConstomerCacheResolver(cacheManager);
	}

}
