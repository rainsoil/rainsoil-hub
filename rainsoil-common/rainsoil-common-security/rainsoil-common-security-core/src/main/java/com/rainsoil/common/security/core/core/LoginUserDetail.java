package com.rainsoil.common.security.core.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 登录用户详情
 *
 * @author luyanan
 * @since 2021/10/3
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserDetail implements Serializable {

	private static final long serialVersionUID = -7139959426143340156L;

	/**
	 * 用户id
	 *
	 * @since 2021/10/3
	 */

	private String userId;

	/**
	 * 用户名
	 *
	 * @since 2021/10/3
	 */

	private String userName;

	/**
	 * 登录名
	 *
	 * @since 2021/10/3
	 */

	private String loginName;

	/**
	 * 密码
	 *
	 * @since 2021/10/3
	 */

	private String password;

	/**
	 * 权限列表
	 *
	 * @since 2021/10/3
	 */

	private Set<String> permissions;

	/**
	 * 角色列表
	 *
	 * @since 2021/10/3
	 */

	private Set<String> roles;

	/**
	 * 附加
	 *
	 * @since 2021/10/3
	 */

	private Map<String, Object> data = new HashMap<>();

	public boolean isAdmin() {
		return "1".equals(this.userId);
	}

}
