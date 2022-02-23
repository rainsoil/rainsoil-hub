package com.rainsoil.system.service;


import com.rainsoil.common.data.mybatis.IBaseService;
import com.rainsoil.system.domain.SysUserRole;

import java.util.List;

/**
 * 用户角色
 *
 * @author luyanan
 * @since 2021/9/27
 **/
public interface ISysUserRoleService extends IBaseService<SysUserRole> {
	/**
	 * 根据角色id和用户集合删除
	 *
	 * @param roleId  角色id
	 * @param userIds 用户集合
	 * @return int
	 * @since 2021/9/29
	 */
	int remove(Long roleId, List<Long> userIds);
}
