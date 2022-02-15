package com.rainsoil.core.datascope;

import com.rainsoil.core.datascope.annotation.DataScope;
import org.aspectj.lang.JoinPoint;

/**
 * 数据权限过滤
 *
 * @author luyanan
 * @since 2021/10/6
 **/
public interface IDataScopeFilter {


	/**
	 * 数据权限过滤
	 *
	 * @param joinPoint
	 * @param dataScope
	 * @return java.lang.String
	 * @since 2021/10/6
	 */
	String dataScopeFilter(JoinPoint joinPoint, DataScope dataScope);
}
