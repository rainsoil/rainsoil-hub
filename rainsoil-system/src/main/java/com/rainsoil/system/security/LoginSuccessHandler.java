package com.rainsoil.system.security;

import cn.hutool.json.JSONUtil;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.common.security.security.config.SecurityProperties;
import com.rainsoil.common.security.security.token.AccessToken;
import com.rainsoil.common.security.security.token.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
@AllArgsConstructor
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final TokenService tokenService;

	private final SecurityProperties securityProperties;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
		httpServletResponse.setContentType("application/json;charset=UTF-8");
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();

		AccessToken accessToken = tokenService.getAccessToken(authentication);
//		// 生成JWT，并放置到请求头中
//		String jwt = jwtUtils.generateToken(authentication.getName());
		httpServletResponse.setHeader(securityProperties.getJwt().getHeader(), accessToken.getValue());

		RespEntity result = RespEntity.success(accessToken);
		outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
		outputStream.flush();
		outputStream.close();
	}
}