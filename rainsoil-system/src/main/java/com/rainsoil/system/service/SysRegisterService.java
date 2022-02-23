package com.rainsoil.system.service;

/**
 * @author luyanan
 * @since 2021/10/12
 **/

import cn.hutool.core.date.DateUtil;

import com.rainsoil.common.framework.spring.SpringContextHolder;
import com.rainsoil.core.constant.UserConstants;
import com.rainsoil.core.module.system.vo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 注册校验方法
 *
 * @author ruoyi
 */

/**
 * 注册校验方法
 *
 * @author ruoyi
 */
@Component
public class SysRegisterService {
	@Autowired
	private ISysUserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 注册
	 */
	public String register(SysUser user) {
		String msg = "", loginName = user.getLoginName(), password = user.getPassword();
//
//		if (!StringUtils.isEmpty(SpringContextHolder.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
//			msg = "验证码错误";
//		} else
//
// TODO 校验验证码
		if (StringUtils.isEmpty(loginName)) {
			msg = "用户名不能为空";
		} else if (StringUtils.isEmpty(password)) {
			msg = "用户密码不能为空";
		} else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
				|| password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
			msg = "密码长度必须在5到20个字符之间";
		} else if (loginName.length() < UserConstants.USERNAME_MIN_LENGTH
				|| loginName.length() > UserConstants.USERNAME_MAX_LENGTH) {
			msg = "账户长度必须在2到20个字符之间";
		} else {
			// 校验用户名
			userService.checkLoginNameUnique(loginName);
			user.setPwdUpdateDate(DateUtil.date());
			user.setUserName(loginName);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
//			user.setSalt(ShiroUtils.randomSalt());
//			user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
			boolean regFlag = userService.registerUser(user);
			if (!regFlag) {
				msg = "注册失败,请联系系统管理人员";
			}
		}
		return msg;
	}
}
