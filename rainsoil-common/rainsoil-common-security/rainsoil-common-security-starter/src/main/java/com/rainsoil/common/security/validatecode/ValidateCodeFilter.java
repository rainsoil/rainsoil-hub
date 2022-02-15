package com.rainsoil.common.security.validatecode;

import cn.hutool.core.collection.CollectionUtil;
import com.rainsoil.common.security.validatecode.config.ValidateCodeProperties;
import com.rainsoil.common.security.validatecode.exception.ValidateCodeErrorException;
import com.rainsoil.common.security.validatecode.exception.ValidateCodeExpireException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 *
 * @author luyanan
 * @since 2021/10/5
 **/

@Slf4j
@ConditionalOnProperty(value = ValidateCodeProperties.PREFIX, name = "validateCodeFilter", havingValue = "true",
		matchIfMissing = false)
public class ValidateCodeFilter extends OncePerRequestFilter {

	@Autowired
	private ValidateCodeProperties validateCodeProperties;

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	private ValidateCodeTemplate validateCodeTemplate;

	/**
	 * Same contract as for {@code doFilter}, but guaranteed to be just invoked once per
	 * request within a single request thread. See {@link #shouldNotFilterAsyncDispatch()}
	 * for details.
	 * <p>
	 * Provides HttpServletRequest and HttpServletResponse arguments instead of the
	 * default ServletRequest and ServletResponse ones.
	 * @param request
	 * @param response
	 * @param filterChain
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (null != validateCodeProperties.getValidateCodeFilter()
				&& validateCodeProperties.getValidateCodeFilter().equals(Boolean.TRUE)
				&& CollectionUtil.isNotEmpty(validateCodeProperties.getValidateCodeUrls())
				&& validateCodeProperties.getValidateCodeUrls().contains(requestURI)
				&& "post".equals(request.getMethod().toLowerCase())) {
			String uuid = request.getParameter(validateCodeProperties.getValidateCodeFilterKeyParameter());
			String code = request.getParameter(validateCodeProperties.getValidateCodeFilterCodeParameter());

			log.debug("开启:{}接口的验证码校验:{}:{},{}:{}", requestURI,
					validateCodeProperties.getValidateCodeFilterKeyParameter(), uuid,
					validateCodeProperties.getValidateCodeFilterCodeParameter(), code);

			try {
				validateCodeTemplate.validate(validateCodeProperties.getCaptchaType(), uuid, code);
			}
			catch (ValidateCodeExpireException e) {
				e.printStackTrace();
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
			}
			catch (ValidateCodeErrorException e) {
				e.printStackTrace();
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
			}

		}
		filterChain.doFilter(request, response);
	}

}
