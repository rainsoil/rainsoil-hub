package com.rainsoil.common.security.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainsoil.common.security.core.core.LoginUserDetail;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息获取抽象类
 *
 * @author luyanan
 * @since 2021/10/5
 **/
@Slf4j
public abstract class AbstractUserDetailsService implements UserDetailsService {

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("{}:开始登录", username);
		LoginUserDetail userDetail = findByUsername(username);
		if (null == userDetail) {
			throw new UsernameNotFoundException("用户:" + username + "不存在");
		}
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(userDetail.getPermissions())) {
			authorities = userDetail.getPermissions().stream().filter(a -> StrUtil.isNotBlank(a))
					.map(a -> new SimpleGrantedAuthority(a)).distinct().collect(Collectors.toList());
		}

		ObjectMapper objectMapper = new ObjectMapper();
		return new User(objectMapper.writeValueAsString(userDetail), userDetail.getPassword(), authorities);
	}

	/**
	 * 根据用户名查询用户信息
	 * @param username 用户名
	 * @return com.rainsoil.security.core.LoginUserDetail
	 * @since 2021/10/5
	 */
	protected abstract LoginUserDetail findByUsername(String username);

}
