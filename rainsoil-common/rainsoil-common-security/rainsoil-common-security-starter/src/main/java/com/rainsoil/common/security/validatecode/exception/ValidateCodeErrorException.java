package com.rainsoil.common.security.validatecode.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误的异常
 *
 * @author luyanan
 * @since 2021/10/5
 **/
public class ValidateCodeErrorException extends AuthenticationException {

	public ValidateCodeErrorException(String msg) {
		super(msg);
	}

}
