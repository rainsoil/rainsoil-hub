package com.rainsoil.system.exception;


import com.rainsoil.core.exception.BaseException;

/**
 * 系统模块异常
 *
 * @author luyanan
 * @since 2021/9/21
 **/
public class SystemException extends BaseException {
	public SystemException(Integer code, Object... args) {
		super("system", code, args);
	}

}
