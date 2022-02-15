package com.rainsoil.common.data.logger;

import com.rainsoil.common.data.logger.annotation.IgnoreLogger;
import com.rainsoil.common.framework.spring.SpringContextHolder;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 日志拦截器
 *
 * @author luyanan
 * @since 2021/8/22
 **/
@Aspect
@Slf4j
@AllArgsConstructor
public class LoggerAspect {

	@Around("@annotation(apiOperation)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, ApiOperation apiOperation) throws Throwable {

		Object[] args = proceedingJoinPoint.getArgs();
		// 开始时间
		long startTime = System.currentTimeMillis();
		// 结果
		Object result = null;
		// 异常
		Throwable e = null;
		try {
			result = proceedingJoinPoint.proceed();
		}
		catch (Throwable throwable) {
			e = throwable;
			throwable.printStackTrace();
			throw throwable;
		}
		finally {
			MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
			Method method = methodSignature.getMethod();
			IgnoreLogger ignoreLogger = method.getAnnotation(IgnoreLogger.class);
			if (isIgnore(ignoreLogger, IgnoreLogger.Type.PARAMS)) {
				args = null;
			}
			if (isIgnore(ignoreLogger, IgnoreLogger.Type.RESULT)) {
				result = null;
			}
			LoggerEventDto loggerEventDto = LoggerEventDto.builder().endTime(System.currentTimeMillis())
					.startTime(startTime).e(e).signature(proceedingJoinPoint.getSignature()).result(result).args(args)
					.build();
			// 发送事件
			SpringContextHolder.publishEvent(new LoggerEvent(loggerEventDto));

		}
		return result;
	}

	/**
	 * 是否忽略
	 * @param ignoreLogger 注解
	 * @param type 类型
	 * @return boolean
	 * @since 2021/8/24
	 */
	private boolean isIgnore(IgnoreLogger ignoreLogger, IgnoreLogger.Type type) {
		return null != ignoreLogger
				&& (ignoreLogger.type().equals(type) || ignoreLogger.type().equals(IgnoreLogger.Type.ALL));
	}

}
