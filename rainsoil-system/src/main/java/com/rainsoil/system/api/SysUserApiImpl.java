package com.rainsoil.system.api;

import com.rainsoil.core.module.system.api.SysUserApi;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SysUserAp接口的实现
 *
 * @author luyanan
 * @since 2021/10/6
 **/
@Service
public class SysUserApiImpl implements SysUserApi {

	@Autowired
	private ISysUserService sysUserService;

	/**
	 * 根据用户id查询用户详情
	 *
	 * @param id 用户id
	 * @return com.rainsoil.core.module.system.vo.SysUser
	 * @since 2021/10/6
	 */
	@Override
	public SysUser getById(Long id) {
		return sysUserService.getById(id);
	}
}
