package com.rainsoil.system.security;//package com.rainsoil.system.security;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @author luyanan
// * @since 2021/10/7
// **/
//@Component
//public class PermissFilter extends OncePerRequestFilter {
//	/**
//	 * Same contract as for {@code doFilter}, but guaranteed to be
//	 * just invoked once per request within a single request thread.
//	 * See {@link #shouldNotFilterAsyncDispatch()} for details.
//	 * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
//	 * default ServletRequest and ServletResponse ones.
//	 *
//	 * @param request
//	 * @param response
//	 * @param filterChain
//	 */
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//		System.out.println(request.getRequestURI() + "------" + response.getStatus());
//	}
//}
