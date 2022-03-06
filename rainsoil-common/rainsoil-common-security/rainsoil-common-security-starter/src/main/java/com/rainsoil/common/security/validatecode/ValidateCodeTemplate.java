package com.rainsoil.common.security.validatecode;

import com.rainsoil.common.security.validatecode.exception.ValidateCodeErrorException;
import com.rainsoil.common.security.validatecode.exception.ValidateCodeExpireException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

/**
 * 验证码生成模板类
 *
 * @author luyanan
 * @since 2021/10/4
 **/
public class ValidateCodeTemplate {

	/**
	 * 验证码处理类
	 *
	 * @since 2021/10/4
	 */

	@Autowired
	private ObjectProvider<ValidateCodeProcessor> validateCodeProcessors;

	/**
	 * 获取处理器
	 *
	 * @param type 验证码类型
	 * @return com.rainsoil.security.validatecode.ValidateCodeProcessor
	 * @since 2021/10/4
	 */
	private ValidateCodeProcessor processor(String type) {
		return getProcessor(type);
	}

	/**
	 * 生成验证码
	 *
	 * @param type       类型
	 * @param key        key
	 * @param expireTime 失效时间
	 * @return java.lang.String
	 * @since 2021/10/4
	 */
	public ValidateCodeProcessor.ValidateCodeResultHandler create(String type, String key, Duration expireTime) {
		return getProcessor(type).create(key, expireTime);
	}

	/**
	 * 根据类型获取验证码处理器
	 *
	 * @param type 类型
	 * @return com.rainsoil.security.validatecode.ValidateCodeProcessor
	 * @since 2021/10/4
	 */
	private ValidateCodeProcessor getProcessor(String type) {
		return validateCodeProcessors.stream()
				.filter(validateCodeProcessor -> validateCodeProcessor.type().equals(type)).findFirst().get();
	}

	/**
	 * 校验验证码
	 *
	 * @param type 类型
	 * @param key  验证码的key
	 * @param code 被校验的验证码
	 * @throws ValidateCodeExpireException 验证码失效
	 * @throws ValidateCodeErrorException  验证码错误
	 * @since 2021/10/5
	 */
	public void validate(String type, String key, String code)
			throws ValidateCodeExpireException, ValidateCodeErrorException {
		ValidateCodeProcessor processor = getProcessor(type);
		processor.validate(key, code);
	}

}
