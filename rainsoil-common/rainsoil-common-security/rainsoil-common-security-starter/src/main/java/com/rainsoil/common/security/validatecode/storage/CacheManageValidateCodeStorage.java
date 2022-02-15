package com.rainsoil.common.security.validatecode.storage;

import com.rainsoil.common.security.core.validatecode.storage.ValidateCodeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.Duration;

/**
 * 验证码存储
 *
 * @author luyanan
 * @since 2021/10/6
 **/
public class CacheManageValidateCodeStorage implements ValidateCodeStorage {

	@Autowired
	private CacheManager cacheManager;

	private static String CACHE_NAME = "VALIDATE_CODE";

	/**
	 * 获取验证码
	 *
	 * @param key key
	 * @return java.lang.String
	 * @since 2021/10/4
	 */
	@Override
	public String getCode(String key) {
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (null == cache) {
			return null;
		}
		Cache.ValueWrapper valueWrapper = cache.get(key);

		return null == valueWrapper ? null : (String) valueWrapper.get();
	}

	/**
	 * 存储验证码
	 *
	 * @param key     key
	 * @param code    验证码
	 * @param timeout 有效时间
	 * @return void
	 * @since 2021/10/4
	 */
	@Override
	public void storage(String key, String code, Duration timeout) {
		Cache cache = cacheManager.getCache(CACHE_NAME);
		if (null == cache) {
			return;
		}
		cache.put(key, code);
	}

}
