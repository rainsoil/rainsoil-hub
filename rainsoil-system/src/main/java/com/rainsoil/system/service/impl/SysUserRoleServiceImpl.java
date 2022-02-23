package com.rainsoil.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.system.domain.SysUserRole;
import com.rainsoil.system.mapper.SysUserRoleMapper;
import com.rainsoil.system.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色
 *
 * @author luyanan
 * @since 2021/9/27
 **/
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
	@Override
	public int remove(Long roleId, List<Long> userIds) {
		if (null == roleId || CollectionUtil.isEmpty(userIds)) {
			return 0;
		}
		boolean remove = super.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId).in(SysUserRole::getUserId, userIds));
		return remove ? 1 : 0;
	}
}
