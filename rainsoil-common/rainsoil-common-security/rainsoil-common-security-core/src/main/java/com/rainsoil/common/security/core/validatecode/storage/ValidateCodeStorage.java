package com.rainsoil.common.security.core.validatecode.storage;

import java.time.Duration;

/**
 * 验证码存储
 *
 * @author luyanan
 * @since 2021/10/4
 **/
public interface ValidateCodeStorage {

	/**
	 * 获取验证码
	 * @param key key
	 * @return java.lang.String
	 * @since 2021/10/4
	 */
	String getCode(String key);

	/**
	 * 存储验证码
	 * @param key key
	 * @param code 验证码
	 * @param timeout 有效时间
	 * @return void
	 * @since 2021/10/4
	 */
	void storage(String key, String code, Duration timeout);

}
