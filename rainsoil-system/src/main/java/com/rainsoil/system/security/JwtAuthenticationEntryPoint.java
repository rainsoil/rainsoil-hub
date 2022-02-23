package com.rainsoil.system.security;

import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.common.framework.spring.SpringContextHolder;
import com.rainsoil.core.message.GlobalCode;
import com.rainsoil.core.utils.ObjectMapperUtils;
import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * 失败
 *
 * @author luyanan
 * @since 2021/10/7
 **/
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
	private static final long serialVersionUID = -8970718410437077606L;

	@SneakyThrows
	@Override
	public void commence(HttpServletRequest request,
						 HttpServletResponse response,
						 AuthenticationException authException) throws IOException {
		//验证为未登陆状态会进入此方法，认证错误
		System.out.println("认证失败：" + authException.getMessage());
		String clientId = SpringContextHolder.getCookieVal(request, "clientId");
		if (StrUtil.isBlank(clientId) || "web".equals(clientId)) {
			response.sendRedirect("/login");
			response.setStatus(302);
		} else {
			response.setStatus(GlobalCode.UNAUTHORIZED);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter printWriter = response.getWriter();
			RespEntity<Object> error = RespEntity.error(GlobalCode.UNAUTHORIZED, authException.getMessage().toString());
			printWriter.write(ObjectMapperUtils.getObjectMapper().writeValueAsString(error));
			printWriter.flush();
		}
	}
}
