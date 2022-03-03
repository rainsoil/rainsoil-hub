package com.rainsoil.common.framework.xss.core;

/**
 * 利用ThreadLocal 缓存线程的数据
 *
 * @author luyanan
 * @since 2021/8/17
 **/
public class XssHolder {

	private static final ThreadLocal<Boolean> TL = new ThreadLocal<>();

	/**
	 * 是否开启
	 *
	 * @return boolean
	 * @since 2022/3/3
	 */
	public static boolean isEnabled() {
		return Boolean.TRUE.equals(TL.get());
	}

	/**
	 * 设置开启
	 *
	 * @since 2022/3/3
	 */
	public static void setEnable() {
		TL.set(Boolean.TRUE);
	}

	/**
	 * 移除
	 *
	 * @since 2022/3/3
	 */
	public static void remove() {
		TL.remove();
	}

}