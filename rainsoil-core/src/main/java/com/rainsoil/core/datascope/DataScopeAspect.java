package com.rainsoil.core.datascope;


import cn.hutool.core.util.StrUtil;
import com.rainsoil.core.datascope.annotation.DataScope;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据过滤处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class DataScopeAspect {

	@Autowired
	private IDataScopeFilter dataScopeFilter;

	// 配置织入点
	@Pointcut("@annotation(com.rainsoil.fastjava.core.datascope.annotation.DataScope)")
	public void dataScopePointCut() {
	}

	@Before("dataScopePointCut()")
	public void doBefore(JoinPoint point) throws Throwable {
		clearDataScope(point);
		handleDataScope(point);
	}

	protected void handleDataScope(final JoinPoint joinPoint) {
		// 获得注解
		DataScope controllerDataScope = getAnnotationLog(joinPoint);
		if (controllerDataScope == null) {
			return;
		}

		String filter = dataScopeFilter.dataScopeFilter(joinPoint, controllerDataScope);
		if (StrUtil.isNotBlank(filter)) {
			DataScopeHolder.set(" AND (" + filter + ")");
		}
	}


	/**
	 * 是否存在注解，如果存在就获取
	 */
	private DataScope getAnnotationLog(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();

		if (method != null) {
			return method.getAnnotation(DataScope.class);
		}
		return null;
	}

	/**
	 * 拼接权限sql前先清空params.dataScope参数防止注入
	 */
	private void clearDataScope(final JoinPoint joinPoint) {
		DataScopeHolder.clear();
	}
}
