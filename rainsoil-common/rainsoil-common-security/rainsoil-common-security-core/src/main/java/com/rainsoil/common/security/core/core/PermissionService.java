package com.rainsoil.common.security.core.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * 自定义权限实现
 *
 * @author luyanan
 * @since 2021/10/4
 **/
public class PermissionService {

	/**
	 * 所有权限标识 //
	 */
	private static final String ALL_PERMISSION = "*:*:*";

	private static final String ROLE_DELIMETER = ",";

	private static final String PERMISSION_DELIMETER = ",";

	@Autowired
	private LoginUserService loginUserService;

	/**
	 * 验证用户是否具有某权限
	 * @param permission 权限字符串
	 * @return boolean 用户是否具备某权限
	 * @since 2021/2/12
	 */
	public boolean hasPermi(String permission) {

		if (StrUtil.isBlank(permission)) {
			return false;
		}
		LoginUserDetail user = loginUserService.getUser();
		if (null == user || CollectionUtil.isEmpty(user.getPermissions())) {
			return false;
		}
		return hasPermissions(user.getPermissions(), permission);

	}

	/**
	 * 验证用户是否不具备某权限,与hasPermi逻辑相反
	 * @param permission 权限字符串
	 * @return boolean 用户是否不具备
	 * @since 2021/2/12
	 */
	public boolean lacksPermi(String permission) {

		return hasPermi(permission) != false;
	}

	/**
	 * 验证用户是否具有以下任意一个权限
	 * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
	 * @return boolean 用户是否具有以下任意一个权限
	 * @since 2021/2/12
	 */
	public boolean hasAnyPermi(String permissions) {

		if (StrUtil.isBlank(permissions)) {
			return false;
		}
		LoginUserDetail user = loginUserService.getUser();
		if (null == user || CollectionUtil.isEmpty(user.getPermissions())) {
			return false;
		}

		Set<String> authorities = user.getPermissions();
		for (String permission : permissions.split(PERMISSION_DELIMETER)) {
			if (permission != null && hasPermissions(authorities, permission)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断用户是否具有某个角色
	 * @param role 角色
	 * @return boolean 用户是否具备某角色
	 * @since 2021/2/12
	 */
	public boolean hasRole(String role) {

		if (StrUtil.isBlank(role)) {
			return false;
		}
		LoginUserDetail user = loginUserService.getUser();
		if (null == user || CollectionUtil.isEmpty(user.getRoles())) {
			return false;
		}

		return user.getRoles().contains(role);
	}

	/**
	 * 验证用户是否不具备某角色,与isRole逻辑相反
	 * @param role 角色名称
	 * @return boolean 用户是否不具备
	 * @since 2021/2/12
	 */
	public boolean lacksRole(String role) {
		return hasRole(role) != true;
	}

	/**
	 * 验证用户是否具有以下任意一个角色
	 * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
	 * @return boolean 用户是否具有以下任意一个角色
	 * @since 2021/2/12
	 */
	public boolean hasAnyRoles(String roles) {

		if (StrUtil.isBlank(roles)) {
			return false;
		}

		LoginUserDetail user = loginUserService.getUser();
		if (null == user || CollectionUtil.isEmpty(user.getRoles())) {
			return false;
		}

		for (String role : roles.split(ROLE_DELIMETER)) {
			if (hasRole(role)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否包含权限
	 * @param permissions 权限列表
	 * @param permission 权限字符串
	 * @return boolean 用户是否具备某权限
	 * @since 2021/2/12
	 */
	private boolean hasPermissions(Set<String> permissions, String permission) {
		return permissions.contains(ALL_PERMISSION) || permissions.contains(StrUtil.trim(permission));
	}

}
