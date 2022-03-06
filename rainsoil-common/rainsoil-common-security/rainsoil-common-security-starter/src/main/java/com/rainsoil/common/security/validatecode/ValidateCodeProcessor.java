package com.rainsoil.common.security.validatecode;


import com.rainsoil.common.security.core.validatecode.storage.ValidateCodeStorage;
import com.rainsoil.common.security.validatecode.exception.ValidateCodeErrorException;
import com.rainsoil.common.security.validatecode.exception.ValidateCodeExpireException;

import java.time.Duration;

/**
 * 验证码处理逻辑
 *
 * @author luyanan
 * @since 2021/10/4
 **/
public interface ValidateCodeProcessor {

	/**
	 * 验证码的类型
	 *
	 * @return java.lang.String
	 * @since 2021/10/4
	 */
	String type();

	/**
	 * 验证码存储
	 *
	 * @return com.rainsoil.security.validatecode.storage.ValidateCodeStorage
	 * @since 2021/10/4
	 */
	ValidateCodeStorage validateCodeStorage();

	/**
	 * 验证码生成
	 *
	 * @param key     key
	 * @param timeout 失效时间
	 * @return java.lang.String base64
	 * @since 2021/10/4
	 */
	ValidateCodeResultHandler create(String key, Duration timeout);

	/**
	 * 验证码校验
	 *
	 * @param key  key
	 * @param code 验证码
	 * @throws ValidateCodeExpireException 验证码失效异常
	 * @throws ValidateCodeErrorException  验证码错误异常
	 * @since 2021/10/4
	 */
	void validate(String key, String code) throws ValidateCodeExpireException, ValidateCodeErrorException;

	/**
	 * 验证码结果处理
	 *
	 * @author luyanan
	 * @since 2022/3/6
	 */
	public interface ValidateCodeResultHandler {

		/**
		 * 返回验证码的base64
		 *
		 * @return java.lang.String
		 * @since 2021/10/5
		 */
		String base64();

		/**
		 * 验证码
		 *
		 * @return java.lang.String
		 * @since 2021/10/5
		 */
		String code();

	}

}
