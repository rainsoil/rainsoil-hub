package com.rainsoil.web;

import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.web.entity.User;
import org.springframework.stereotype.Service;

/**
 * @author luyanan
 * @since 2022/2/23
 **/
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {
}
