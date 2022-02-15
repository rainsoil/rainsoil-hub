package com.rainsoil.core.module.system.api;


import com.rainsoil.core.module.system.vo.SysUser;

/**
 * 用户
 *
 * @author luyanan
 * @since 2021/10/6
 **/
public interface SysUserApi {


	/**
	 * 根据用户id查询用户详情
	 *
	 * @param id 用户id
	 * @return com.rainsoil.fastjava.core.module.system.vo.SysUser
	 * @since 2021/10/6
	 */
	SysUser getById(Long id);
}
