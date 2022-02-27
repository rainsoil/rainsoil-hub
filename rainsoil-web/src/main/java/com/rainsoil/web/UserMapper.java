package com.rainsoil.web;

import com.rainsoil.common.data.mybatis.BaseMapperPlus;
import com.rainsoil.web.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author luyanan
 * @since 2022/2/23
 **/
@Mapper
public interface UserMapper extends BaseMapperPlus<User> {
}
