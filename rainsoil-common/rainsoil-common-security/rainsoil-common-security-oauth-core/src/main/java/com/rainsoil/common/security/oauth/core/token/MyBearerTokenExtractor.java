package com.rainsoil.common.security.oauth.core.token;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 扩展Token的获取方法
 *
 * @author luyanan
 * @since 2021/10/25
 **/
@Slf4j
@RequiredArgsConstructor
public class MyBearerTokenExtractor extends BearerTokenExtractor {

	@Override
	protected String extractToken(HttpServletRequest request) {
		// first check the header...
		String token = extractHeaderToken(request);
		String tokenKey = OAuth2AccessToken.ACCESS_TOKEN;
		// bearer type allows a request parameter as well
		if (token == null) {
			log.warn("Token not found in headers. Trying request parameters.");
			token = request.getParameter(tokenKey);
			if (token == null) {
				token = ServletUtil.getCookie(request, tokenKey) == null ? null
						: ServletUtil.getCookie(request, tokenKey).getValue();
				log.warn("Token not found in request parameters.  Not an OAuth2 request.");
			}
			else {
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, OAuth2AccessToken.BEARER_TYPE);
			}
		}
		return token;
	}

}
