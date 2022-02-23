package com.rainsoil.system.security;

import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.security.core.core.LoginUserDetail;
import com.rainsoil.common.security.security.AbstractUserDetailsService;
import com.rainsoil.core.constant.UserConstants;
import com.rainsoil.core.module.system.vo.SysMenu;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.system.service.ISysMenuService;
import com.rainsoil.system.service.ISysRoleService;
import com.rainsoil.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息查询
 *
 * @author luyanan
 * @since 2021/10/6
 **/
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl extends AbstractUserDetailsService {

	private final ISysUserService sysUserService;

	private final ISysRoleService sysRoleService;


	private final ISysMenuService sysMenuService;

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param username 用户名
	 * @return com.rainsoil.security.core.LoginUserDetail
	 * @since 2021/10/5
	 */
	@Override
	protected LoginUserDetail findByUsername(String username) {
		SysUser sysUser = sysUserService.selectUserByLoginName(username);
		if (null == sysUser) {
			throw new UsernameNotFoundException("用户不存在");
		}
		// 判断用户状态
		if (!sysUser.getStatus().equals(UserConstants.NORMAL)) {
			throw new LockedException("用户被锁定");
		}

		// 查询角色
		List<SysRole> sysRoles = sysRoleService.selectRolesByUserId(sysUser.getUserId());
		Set<String> meuns = null;
		// 查询菜单权限
		if (sysUser.isAdmin()) {
			// 查询所有
			meuns = sysMenuService.list().stream().filter(a -> StrUtil.isNotBlank(a.getPerms())).map(SysMenu::getPerms).distinct().collect(Collectors.toSet());
		} else {

			meuns = sysMenuService.selectPermsByUserId(sysUser.getUserId());
		}

		return LoginUserDetail.builder().loginName(sysUser.getLoginName()).password(sysUser.getPassword()).permissions(meuns)
				.roles(sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toSet())).userId(sysUser.getUserId() + "")
				.userName(sysUser.getUserName()).build();
	}
}
