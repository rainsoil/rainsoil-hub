package com.rainsoil.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainsoil.system.domain.SysRoleDept;
import com.rainsoil.system.mapper.SysRoleDeptMapper;
import com.rainsoil.system.service.ISysRoleDeptService;
import org.springframework.stereotype.Service;

/**
 * 角色和部门关联
 *
 * @author luyanan
 * @since 2021/9/28
 **/
@Service
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptMapper, SysRoleDept> implements ISysRoleDeptService {
}
