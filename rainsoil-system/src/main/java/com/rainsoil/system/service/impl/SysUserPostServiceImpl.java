package com.rainsoil.system.service.impl;


import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.system.domain.SysUserPost;
import com.rainsoil.system.mapper.SysUserPostMapper;
import com.rainsoil.system.service.ISysUserPostService;
import org.springframework.stereotype.Service;

/**
 * 用户岗位
 *
 * @author luyanan
 * @since 2021/9/23
 **/
@Service
public class SysUserPostServiceImpl extends BaseServiceImpl<SysUserPostMapper, SysUserPost> implements ISysUserPostService {
}
