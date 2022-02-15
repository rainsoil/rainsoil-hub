package com.rainsoil.common.security.validatecode.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码过期的异常
 *
 * @author luyanan
 * @since 2021/10/5
 **/
public class ValidateCodeExpireException extends AuthenticationException {

	public ValidateCodeExpireException(String msg) {
		super(msg);
	}

}
