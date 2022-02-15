package com.rainsoil.common.security.oauth.server.exception;

import com.rainsoil.common.security.oauth.core.exception.CustomOauthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * 自定义异常转换器
 *
 * @author luyanan
 * @since 2021/10/26
 **/
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

	@Override
	public ResponseEntity translate(Exception exception) throws Exception {
		if (exception instanceof OAuth2Exception) {
			OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
			if (exception instanceof ClientAuthenticationException) {
				return ResponseEntity.status(HttpStatus.OK).body(new CustomOauthException("用户名或者密码错误"));
			}
			else {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new CustomOauthException(oAuth2Exception.getMessage()));
			}

		}
		else if (exception instanceof AuthenticationException) {
			AuthenticationException authenticationException = (AuthenticationException) exception;
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomOauthException(authenticationException.getMessage()));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new CustomOauthException(exception.getMessage()));
	}

}
