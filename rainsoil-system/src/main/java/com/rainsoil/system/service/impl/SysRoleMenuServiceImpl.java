package com.rainsoil.system.service.impl;


import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.system.domain.SysRoleMenu;
import com.rainsoil.system.mapper.SysRoleMenuMapper;
import com.rainsoil.system.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色菜单
 *
 * @author luyanan
 * @since 2021/9/27
 **/
@Service
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {
}
