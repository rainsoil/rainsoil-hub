package com.rainsoil.system.service;

import cn.hutool.core.util.ReflectUtil;
import com.rainsoil.common.security.core.core.LoginUserDetail;
import com.rainsoil.common.security.core.core.LoginUserServices;
import com.rainsoil.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * RuoYi首创 js调用 thymeleaf 实现按钮权限可见性
 *
 * @author ruoyi
 */
@Slf4j
@Service("permission")
public class PermissionPageService {

	/**
	 * 没有权限，hidden用于前端隐藏按钮
	 */
	public static final String NOACCESS = "hidden";

	private static final String ROLE_DELIMETER = ",";

	private static final String PERMISSION_DELIMETER = ",";

	/**
	 * 验证用户是否具备某权限，无权限返回hidden用于前端隐藏（如需返回Boolean使用isPermitted）
	 *
	 * @param permission 权限字符串
	 * @return 用户是否具备某权限
	 */
	public String hasPermi(String permission) {
		return isPermitted(permission) ? StringUtil.EMPTY : NOACCESS;
	}

	/**
	 * 验证用户是否不具备某权限，与 hasPermi逻辑相反。无权限返回hidden用于前端隐藏（如需返回Boolean使用isLacksPermitted）
	 *
	 * @param permission 权限字符串
	 * @return 用户是否不具备某权限
	 */
	public String lacksPermi(String permission) {
		return isLacksPermitted(permission) ? StringUtil.EMPTY : NOACCESS;
	}

	/**
	 * 验证用户是否具有以下任意一个权限，无权限返回hidden用于隐藏（如需返回Boolean使用hasAnyPermissions）
	 *
	 * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
	 * @return 用户是否具有以下任意一个权限
	 */
	public String hasAnyPermi(String permissions) {
		return hasAnyPermissions(permissions, PERMISSION_DELIMETER) ? StringUtil.EMPTY : NOACCESS;
	}

	/**
	 * 验证用户是否具备某角色，无权限返回hidden用于隐藏（如需返回Boolean使用isRole）
	 *
	 * @param role 角色字符串
	 * @return 用户是否具备某角色
	 */
	public String hasRole(String role) {
		return isRole(role) ? StringUtil.EMPTY : NOACCESS;
	}

	/**
	 * 验证用户是否不具备某角色，与hasRole逻辑相反。无权限返回hidden用于隐藏（如需返回Boolean使用isLacksRole）
	 *
	 * @param role 角色字符串
	 * @return 用户是否不具备某角色
	 */
	public String lacksRole(String role) {
		return isLacksRole(role) ? StringUtil.EMPTY : NOACCESS;
	}

	/**
	 * 验证用户是否具有以下任意一个角色，无权限返回hidden用于隐藏（如需返回Boolean使用isAnyRoles）
	 *
	 * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
	 * @return 用户是否具有以下任意一个角色
	 */
	public String hasAnyRoles(String roles) {
		return isAnyRoles(roles, ROLE_DELIMETER) ? StringUtil.EMPTY : NOACCESS;
	}


	/**
	 * 判断用户是否拥有某个权限
	 *
	 * @param permission 权限字符串
	 * @return 用户是否具备某权限
	 */
	public boolean isPermitted(String permission) {
		return LoginUserServices.getUser().getPermissions().contains(permission);
	}

	/**
	 * 判断用户是否不具备某权限，与 isPermitted逻辑相反。
	 *
	 * @param permission 权限名称
	 * @return 用户是否不具备某权限
	 */
	public boolean isLacksPermitted(String permission) {
		return isPermitted(permission) != true;
	}

	/**
	 * 验证用户是否具有以下任意一个权限。
	 *
	 * @param permissions 以 PERMISSION_NAMES_DELIMETER 为分隔符的权限列表
	 * @return 用户是否具有以下任意一个权限
	 */
	public boolean hasAnyPermissions(String permissions) {
		return hasAnyPermissions(permissions, PERMISSION_DELIMETER);
	}

	/**
	 * 验证用户是否具有以下任意一个权限。
	 *
	 * @param permissions 以 delimeter 为分隔符的权限列表
	 * @param delimeter   权限列表分隔符
	 * @return 用户是否具有以下任意一个权限
	 */
	public boolean hasAnyPermissions(String permissions, String delimeter) {
		LoginUserDetail user = LoginUserServices.getUser();

		if (user != null) {
			if (delimeter == null || delimeter.length() == 0) {
				delimeter = PERMISSION_DELIMETER;
			}

			for (String permission : permissions.split(delimeter)) {
				if (permission != null && user.getPermissions().contains(permission)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 判断用户是否拥有某个角色
	 *
	 * @param role 角色字符串
	 * @return 用户是否具备某角色
	 */
	public boolean isRole(String role) {
		return LoginUserServices.getUser().getRoles().contains(role);
	}

	/**
	 * 验证用户是否不具备某角色，与 isRole逻辑相反。
	 *
	 * @param role 角色名称
	 * @return 用户是否不具备某角色
	 */
	public boolean isLacksRole(String role) {
		return isRole(role) != true;
	}

	/**
	 * 验证用户是否具有以下任意一个角色。
	 *
	 * @param roles 以 ROLE_NAMES_DELIMETER 为分隔符的角色列表
	 * @return 用户是否具有以下任意一个角色
	 */
	public boolean isAnyRoles(String roles) {
		return isAnyRoles(roles, ROLE_DELIMETER);
	}

	/**
	 * 验证用户是否具有以下任意一个角色。
	 *
	 * @param roles     以 delimeter 为分隔符的角色列表
	 * @param delimeter 角色列表分隔符
	 * @return 用户是否具有以下任意一个角色
	 */
	public boolean isAnyRoles(String roles, String delimeter) {
		LoginUserDetail user = LoginUserServices.getUser();
		if (user != null) {
			if (delimeter == null || delimeter.length() == 0) {
				delimeter = ROLE_DELIMETER;
			}

			for (String role : roles.split(delimeter)) {
				if (user.getRoles().contains(role.trim())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 返回用户属性值
	 *
	 * @param property 属性名称
	 * @return 用户属性值
	 */
	public Object getPrincipalProperty(String property) {
		LoginUserDetail user = LoginUserServices.getUser();
		if (user != null) {
			return ReflectUtil.getFieldValue(user, property);

//			Object principal = subject.getPrincipal();
//			try {
//				BeanInfo bi = Introspector.getBeanInfo(principal.getClass());
//				for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
//					if (pd.getName().equals(property) == true) {
//						return pd.getReadMethod().invoke(principal, (Object[]) null);
//					}
//				}
//			} catch (Exception e) {
//				log.error("Error reading property [{}] from principal of type [{}]", property, principal.getClass().getName());
//			}
		}
		return null;
	}
}
