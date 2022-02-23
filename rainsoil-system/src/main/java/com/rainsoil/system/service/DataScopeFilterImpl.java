package com.rainsoil.system.service;

import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.security.core.core.LoginUserServices;
import com.rainsoil.core.datascope.IDataScopeFilter;
import com.rainsoil.core.datascope.annotation.DataScope;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.utils.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据权限过滤实现类
 *
 * @author luyanan
 * @since 2021/10/6
 **/
@Component
public class DataScopeFilterImpl implements IDataScopeFilter {
	/**
	 * 全部数据权限
	 */
	public static final String DATA_SCOPE_ALL = "1";

	/**
	 * 自定数据权限
	 */
	public static final String DATA_SCOPE_CUSTOM = "2";

	/**
	 * 部门数据权限
	 */
	public static final String DATA_SCOPE_DEPT = "3";

	/**
	 * 部门及以下数据权限
	 */
	public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

	/**
	 * 仅本人数据权限
	 */
	public static final String DATA_SCOPE_SELF = "5";

	@Autowired
	private ISysRoleService sysRoleService;


	@Autowired
	private ISysUserService sysUserService;

	/**
	 * 数据权限过滤
	 *
	 * @param joinPoint
	 * @param dataScopeAnn
	 * @return java.lang.String
	 * @since 2021/10/6
	 */
	@Override
	public String dataScopeFilter(JoinPoint joinPoint, DataScope dataScopeAnn) {
		String userId = LoginUserServices.getUserId();
		if (StrUtil.isBlank(userId)) {
			return " 1 = 1 ";
		}
		if ("1".equalsIgnoreCase(userId)) {

			// 超级管理员
			return " 1 = 1  ";
		}


		SysUser sysUser = sysUserService.getById(Long.valueOf(userId));
		// 查询角色
		List<SysRole> sysRoles = sysRoleService.selectRolesByUserId(Long.valueOf(userId));
		StringBuilder sqlString = new StringBuilder();
		for (SysRole role : sysRoles) {
			String dataScope = role.getDataScope();
			if (DATA_SCOPE_ALL.equals(dataScope)) {
				sqlString = new StringBuilder();
				break;
			} else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
				sqlString.append(StringUtil.format(
						" OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", dataScopeAnn.deptAlias(),
						role.getRoleId()));
			} else if (DATA_SCOPE_DEPT.equals(dataScope)) {
				sqlString.append(StringUtil.format(" OR {}.dept_id = {} ", dataScopeAnn.deptAlias(), sysUser.getDeptId()));
			} else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
				sqlString.append(StringUtil.format(
						" OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
						dataScopeAnn.deptAlias(), sysUser.getDeptId(), sysUser.getDeptId()));
			} else if (DATA_SCOPE_SELF.equals(dataScope)) {
				if (StringUtil.isNotBlank(dataScopeAnn.userAlias())) {
					sqlString.append(StringUtil.format(" OR {}.user_id = {} ", dataScopeAnn.userAlias(), sysUser.getUserId()));
				} else {
					// 数据权限为仅本人且没有userAlias别名不查询任何数据
					sqlString.append(" OR 1=0 ");
				}
			}
		}
		return sqlString.toString();
	}
}
