package com.rainsoil.system.security;

import cn.hutool.json.JSONUtil;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.common.security.security.config.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author luyanan
 * @since 2022/2/27
 **/
@Component
public class JWTLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	SecurityProperties securityProperties;

	@Override
	public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
		}

		httpServletResponse.setContentType("application/json;charset=UTF-8");
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();

		httpServletResponse.setHeader(securityProperties.getJwt().getHeader(), "");


		outputStream.write(JSONUtil.toJsonStr(RespEntity.success()).getBytes(StandardCharsets.UTF_8));
		outputStream.flush();
		outputStream.close();
	}
}