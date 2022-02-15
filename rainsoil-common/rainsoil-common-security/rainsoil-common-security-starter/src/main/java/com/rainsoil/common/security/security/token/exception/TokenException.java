package com.rainsoil.common.security.security.token.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * token异常类
 *
 * @author luyanan
 * @since 2021/10/10
 **/
public class TokenException extends AuthenticationException {

	public TokenException(String msg) {
		super(msg);
	}

}
