package com.rainsoil.system.security;

import cn.hutool.json.JSONUtil;
import com.rainsoil.common.core.page.RespEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
		httpServletResponse.setContentType("application/json;charset=UTF-8");
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();

		String errorMessage = "用户名或密码错误";
		RespEntity result;

		result = RespEntity.error(100, errorMessage);
		outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
		outputStream.flush();
		outputStream.close();
	}
}