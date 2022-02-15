package com.rainsoil.common.framework.xss.core;

/**
 * 利用ThreadLocal 缓存线程的数据
 *
 * @author luyanan
 * @since 2021/8/17
 **/
public class XssHolder {

	private static final ThreadLocal<Boolean> TL = new ThreadLocal<>();

	public static boolean isEnabled() {
		return Boolean.TRUE.equals(TL.get());
	}

	public static void setEnable() {
		TL.set(Boolean.TRUE);
	}

	public static void remove() {
		TL.remove();
	}

}