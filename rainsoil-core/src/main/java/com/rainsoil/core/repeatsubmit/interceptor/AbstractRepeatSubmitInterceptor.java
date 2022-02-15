package com.rainsoil.core.repeatsubmit.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.rainsoil.fastjava.core.repeatsubmit.annotation.RepeatSubmit;
import com.rainsoil.fastjava.common.framework.core.RespEntity;
import com.rainsoil.fastjava.common.framework.mssage.GlobalCode;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 防止重复提交拦截器
 *
 * @author ruoyi
 */
@Component
public abstract class AbstractRepeatSubmitInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
			if (annotation != null) {
				if (this.isRepeatSubmit(request)) {
					RespEntity ajaxResult = RespEntity.error(GlobalCode.INTERNAL_SERVER_ERROR, "不允许重复提交，请稍后再试");
					ServletUtil.write(response, JSON.toJSONString(ajaxResult), "application/json");
					return false;
				}
			}
			return true;
		} else {
			return super.preHandle(request, response, handler);
		}
	}

	/**
	 * 验证是否重复提交由子类实现具体的防重复提交的规则
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public abstract boolean isRepeatSubmit(HttpServletRequest request) throws Exception;
}
