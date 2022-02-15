package com.rainsoil.common.security.oauth.core.token;// package com.rainsoil.fastjava.security.oauth.core.token;
//
// import cn.hutool.core.util.StrUtil;
// import com.rainsoil.fastjava.common.security.security.token.AccessToken;
// import com.rainsoil.fastjava.common.security.security.token.TokenService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.oauth2.common.OAuth2AccessToken;
// import org.springframework.security.oauth2.provider.OAuth2Authentication;
// import
// org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
// import org.springframework.security.oauth2.provider.token.TokenStore;
//
/// **
// * oauth的token存储
// *
// * @author luyanan
// * @since 2021/10/27
// **/
// @RequiredArgsConstructor
// public class OauthTokenStore implements TokenService {
// private final AuthorizationServerTokenServices authorizationServerTokenServices;
//
// private final TokenStore tokenStore;
//
// /**
// * 读取token
// *
// * @param token
// * @return com.rainsoil.fastjava.security.core.LoginUserDetail
// * @since 2021/10/10
// */
// @Override
// public Authentication readToken(String token) {
// if (StrUtil.isBlank(token)) {
// return null;
// }
//// OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
// OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
// return oAuth2Authentication;
// }
//
// /**
// * 生成token
// *
// * @param authentication 用户信息
// * @return com.rainsoil.fastjava.security.security.token.AccessToken
// * @since 2021/10/10
// */
// @Override
// public AccessToken getAccessToken(Authentication authentication) {
// OAuth2Authentication oAuth2Authentication = null;
// if (authentication instanceof UsernamePasswordAuthenticationToken) {
// UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
// (UsernamePasswordAuthenticationToken) authentication;
// oAuth2Authentication = new OAuth2Authentication(null, authentication);
// } else {
// oAuth2Authentication = (OAuth2Authentication) authentication;
// }
//// OAuth2AccessToken auth2AccessToken = tokenStore.getAccessToken(oAuth2Authentication);
//
// OAuth2AccessToken auth2AccessToken =
// authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
//
// AccessToken accessToken = new AccessToken();
// accessToken.setExpiresIn(auth2AccessToken.getExpiresIn());
// accessToken.setValue(auth2AccessToken.getValue());
// return accessToken;
// }
//
// /**
// * 校验token
// *
// * @param accessToken
// * @return boolean
// * @since 2021/10/10
// */
// @Override
// public boolean validateAccessToken(String accessToken) {
//
// OAuth2AccessToken auth2AccessToken = tokenStore.readAccessToken(accessToken);
// return null != auth2AccessToken;
// }
//
// /**
// * 移除token
// *
// * @param token token
// * @return void
// * @since 2021/10/10
// */
// @Override
// public void removeToken(String token) {
//
// }
//
// /**
// * 刷新token
// *
// * @param refreshToken token
// * @return com.rainsoil.fastjava.security.security.token.AccessToken
// * @since 2021/10/10
// */
// @Override
// public AccessToken refreshToken(String refreshToken) {
// return null;
// }
//
// /**
// * 校验刷新的token
// *
// * @param refreshToken
// * @return boolean
// * @since 2021/10/10
// */
// @Override
// public boolean validateRefreshToken(String refreshToken) {
// return false;
// }
// }
