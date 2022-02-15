package com.rainsoil.core.datascope;

import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 数据权限Holder
 *
 * @author luyanan
 * @since 2021/10/2
 **/
public class DataScopeHolder {


	private static ThreadLocal<String> scopeLocal = new TransmittableThreadLocal();


	public static void clear() {
		scopeLocal.remove();
	}


	public static void set(String sql) {
		if (StrUtil.isNotBlank(sql)) {
			scopeLocal.set(sql);
		}
	}

	public static String get() {
		return scopeLocal.get();
	}

}
