package com.rainsoil.system.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.rainsoil.common.framework.spring.SpringContextHolder;
import com.rainsoil.common.security.security.config.JwtProperties;
import com.rainsoil.common.security.security.config.SecurityProperties;
import com.rainsoil.common.security.security.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * token校验
 *
 * @author luyanan
 * @since 2021/10/7
 **/
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenService tokenService;

	/**
	 * Same contract as for {@code doFilter}, but guaranteed to be
	 * just invoked once per request within a single request thread.
	 * See {@link #shouldNotFilterAsyncDispatch()} for details.
	 * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
	 * default ServletRequest and ServletResponse ones.
	 *
	 * @param request
	 * @param response
	 * @param filterChain
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		JwtProperties jwt = securityProperties.getJwt();

		String token = request.getHeader(jwt.getHeader());
		if (StrUtil.isBlank(token)) {
			// 从请求参数中获取
			token = request.getParameter(jwt.getHeader());
		}
		if (StrUtil.isBlank(token)) {
			// 从cookie中获取
			token = SpringContextHolder.getCookieVal(request, jwt.getHeader());
			token = URLUtil.decode(token, Charset.forName("utf-8"));
		}

		if (StrUtil.isNotBlank(token) && token.startsWith(jwt.getTokenHead())) {
			token = token.substring(jwt.getTokenHead().length());
		} else {
			token = null;
		}
		Authentication tokenAuthentication = tokenService.readToken(token);

		if (tokenAuthentication != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			if (tokenService.validateAccessToken(token)) {
//				UserDetails userDetails = userDetailsService.loadUserByUsername(loginName);
//				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
//						null, userDetails.getAuthorities());
//				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				logger.info(String.format("Authenticated user %s, setting security context", userDetails));
				SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
			}
		}
		filterChain.doFilter(request, response);
	}
}
