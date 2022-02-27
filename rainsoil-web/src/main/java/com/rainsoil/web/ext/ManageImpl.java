package com.rainsoil.web.ext;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * manage实现
 *
 * @author luyanan
 * @since 2021/9/19
 **/
public class ManageImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IManage<T> {


}
