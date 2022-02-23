package com.rainsoil.common.security.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainsoil.common.framework.spring.SpringContextHolder;
import com.rainsoil.common.security.core.core.LoginUserDetail;
import com.rainsoil.common.security.core.core.LoginUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

/**
 * 登录用户实现类
 *
 * @author luyanan
 * @since 2021/10/6
 **/
@Slf4j
public class LoginUserServiceImpl implements LoginUserService {

	/**
	 * 获取登录用户
	 *
	 * @param required 是否必须
	 * @return com.rainsoil.security.core.LoginUserDetail
	 * @since 2021/10/3
	 */
	@Override
	public LoginUserDetail getUser(boolean required) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			if (required) {
				throw new AccountExpiredException("请重新登录");
			} else {
				return null;
			}
		}

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (isNotLogin(authentication, principal)) {
			if (required) {
				throw new AccountExpiredException("请重新登录");
			} else {
				return null;
			}
		}
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			if (authentication instanceof UsernamePasswordAuthenticationToken) {


				String username = objectMapper.readValue(principal.toString(), new TypeReference<Map<String, Object>>() {
				}).get("username").toString();

				return objectMapper.readValue(username, LoginUserDetail.class);
			} else {
				return objectMapper.readValue(principal.toString(), LoginUserDetail.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取用户信息失败:{}", e);
			return null;
		}
	}

	/**
	 * 锁定
	 *
	 * @since 2022/2/20
	 */
	@Override
	public void lock() {

		// TODO 锁定操作

	}

	/**
	 * 解锁
	 *
	 * @since 2022/2/20
	 */
	@Override
	public void unLock() {
		// TODO 解锁
	}

	/**
	 * 判断是否没有登录
	 *
	 * @param authentication authentication
	 * @param principal      principal
	 * @return boolean
	 * @since 2022/2/9
	 */
	private boolean isNotLogin(Authentication authentication, Object principal) {
		return StrUtil.isBlank(authentication.getName()) || null == principal
				|| (principal instanceof String && "anonymousUser".equals(principal.toString()));
	}

}
