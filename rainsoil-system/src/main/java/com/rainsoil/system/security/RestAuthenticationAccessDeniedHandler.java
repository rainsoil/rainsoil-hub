package com.rainsoil.system.security;

import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.core.message.GlobalCode;
import com.rainsoil.core.utils.ObjectMapperUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 没有权限
 *
 * @author luyanan
 * @since 2021/10/7
 **/
@Component("RestAuthenticationAccessDeniedHandler")
public class RestAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request,
					   HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
		//登陆状态下，权限不足执行该方法
		System.out.println("权限不足：" + e.getMessage());
		response.setStatus(403);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter printWriter = response.getWriter();
		String body = ObjectMapperUtils.getObjectMapper().writeValueAsString(RespEntity.error(GlobalCode.UNAUTHORIZED, e.getMessage()));
		printWriter.write(body);
		printWriter.flush();
//		throw new AccessDeniedException("没有权限");
	}
}
