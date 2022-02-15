package com.rainsoil.common.security.oauth.client.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 授权失败之后的返回信息
 *
 * @author luyanan
 * @since 2021/10/26
 **/
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	/**
	 * Handles an access denied failure.
	 * @param request that resulted in an <code>AccessDeniedException</code>
	 * @param response so that the user agent can be advised of the failure
	 * @param accessDeniedException that caused the invocation
	 * @throws IOException in the event of an IOException
	 * @throws ServletException in the event of a ServletException
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		Map<String, Object> map = new HashMap<>(2);
		map.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		map.put("msg", "没有权限");
		ObjectMapper objectMapper = new ObjectMapper();
		response.getWriter().write(objectMapper.writeValueAsString(map));
	}

}
