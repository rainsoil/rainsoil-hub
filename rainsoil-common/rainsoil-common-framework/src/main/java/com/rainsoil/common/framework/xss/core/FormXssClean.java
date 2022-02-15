package com.rainsoil.common.framework.xss.core;

import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.framework.xss.utils.XssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * 表单xss处理
 *
 * @author luyanan
 * @since 2021/8/17
 **/
@ControllerAdvice
public class FormXssClean {

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {

		// 处理前端传来的表单字符串
		webDataBinder.registerCustomEditor(String.class, new StringPropertiesEditor());
	}

	@Slf4j
	public static class StringPropertiesEditor extends PropertyEditorSupport {

		@Override
		public String getAsText() {
			Object value = getValue();
			return value != null ? value.toString() : StrUtil.EMPTY;
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (text == null) {
				setValue(null);
			}
			else if (XssHolder.isEnabled()) {
				String value = XssUtil.clean(text);
				setValue(value);
				log.trace("Request parameter value:{} cleaned up by mica-xss, current value is:{}.", text, value);
			}
			else {
				setValue(text);
			}
		}

	}

}
