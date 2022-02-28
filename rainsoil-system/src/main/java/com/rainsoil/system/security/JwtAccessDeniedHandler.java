package com.rainsoil.system.security;

import cn.hutool.json.JSONUtil;
import com.rainsoil.common.core.page.RespEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
		httpServletResponse.setContentType("application/json;charset=UTF-8");
		httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();


		outputStream.write(JSONUtil.toJsonStr(RespEntity.error(500, e.getLocalizedMessage())).getBytes(StandardCharsets.UTF_8));
		outputStream.flush();
		outputStream.close();
	}
}