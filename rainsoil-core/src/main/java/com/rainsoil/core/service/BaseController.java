package com.rainsoil.core.service;

import cn.hutool.core.date.DateUtil;
import com.rainsoil.common.framework.spring.SpringContextHolder;
import com.rainsoil.common.security.core.core.LoginUserDetail;
import com.rainsoil.common.security.core.core.LoginUserServices;
import com.rainsoil.core.module.system.api.SysUserApi;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.utils.StringUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * web层通用数据处理
 *
 * @author luyanan
 * @since 2022/2/20
 **/
public class BaseController {
	/**
	 * 将前台传递过来的日期格式的字符串，自动转化为Date类型
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtil.parseDate(text));
			}
		});
	}


	/**
	 * 获取request
	 */
	public HttpServletRequest getRequest() {
		return SpringContextHolder.getRequest();
	}

	/**
	 * 获取response
	 */
	public HttpServletResponse getResponse() {
		return SpringContextHolder.getResponse();
	}

	/**
	 * 获取session
	 */
	public HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 页面跳转
	 */
	public String redirect(String url) {
		return StringUtil.format("redirect:{}", url);
	}


	/**
	 * 获取用户详情
	 *
	 * @return com.rainsoil.core.module.system.vo.SysUser
	 * @since 2021/10/6
	 */
	public SysUser getSysUser() {
		LoginUserDetail user = LoginUserServices.getUser(true);
		return SpringContextHolder.getBean(SysUserApi.class).getById(Long.valueOf(user.getUserId()));
	}


	/**
	 * 用户id
	 *
	 * @return java.lang.Long
	 * @since 2021/10/6
	 */
	public Long getUserId() {
		return Long.valueOf(LoginUserServices.getUserId(true));
	}
}
