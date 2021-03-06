package com.rainsoil.common.security.security.token;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainsoil.common.security.security.config.JwtProperties;
import com.rainsoil.common.security.security.token.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 使用jwt存储token
 *
 * @author luyanan
 * @since 2021/10/10
 **/
@AllArgsConstructor
public class JwtTokenServiceImpl implements TokenService {

	private final ObjectMapper objectMapper;
	public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";

	public static final String ROLE_TOKEN = "ROLE_TOKEN";

	private static final String CLAIM_KEY_USER_ID = "user_id";

	private static final String CLAIM_KEY_USER = "user";

	private static final String CLAIM_KEY_AUTHORITIES = "scope";

	private final JwtProperties jwtProperties;

	private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

	/**
	 * 读取token
	 *
	 * @param token
	 * @return com.rainsoil.fastjava.security.core.LoginUserDetail
	 * @since 2021/10/10
	 */
	@SneakyThrows
	@Override
	public Authentication readToken(String token) {
		Claims claims = getClaimsFromToken(token);
		if (null == claims) {
			return null;
		}
		ObjectMapper objectMapper = new ObjectMapper();

		String clazzName = claims.get("class").toString();
		return (Authentication) objectMapper.readValue(claims.get("user").toString(), Class.forName(clazzName));
	}

	/**
	 * 生成token
	 *
	 * @param authentication 用户信息
	 * @return com.rainsoil.fastjava.security.security.token.AccessToken
	 * @since 2021/10/10
	 */
	@Override
	public AccessToken getAccessToken(Authentication authentication) {
		return generateAccessToken(authentication);
	}

	/**
	 * 校验token
	 *
	 * @param accessToken
	 * @return boolean
	 * @since 2021/10/10
	 */
	@Override
	public boolean validateAccessToken(String accessToken) {
		Claims claims = getClaimsFromToken(accessToken);
		return checkAccessToke(claims);
	}

	/**
	 * 校验AccessToken的角色
	 *
	 * @param claims
	 * @return boolean
	 * @since 2021/10/11
	 */
	private boolean checkAccessToke(Claims claims) {
		if (claims.get(CLAIM_KEY_AUTHORITIES, String.class).equals(ROLE_TOKEN)) {
			return true;
		}
		return checkExpireTime(claims);
	}

	/**
	 * 判断token是否过期
	 *
	 * @param claims claims
	 * @return boolean
	 * @since 2021/10/10
	 */
	protected boolean checkExpireTime(Claims claims) {
		return claims.getExpiration().before(new Date());
	}

	/**
	 * 生成AccessToken token
	 *
	 * @param authentication
	 * @return com.rainsoil.fastjava.security.security.token.AccessToken
	 * @since 2021/10/10
	 */
	@SneakyThrows
	protected AccessToken generateAccessToken(Authentication authentication) {
		Assert.notNull(authentication, "authentication  not null");

		Map<String, Object> claims = new HashMap<>(2);
		claims.put(CLAIM_KEY_USER, objectMapper.writeValueAsString(authentication));
		claims.put("class", authentication.getClass());
		claims.put(CLAIM_KEY_AUTHORITIES, ROLE_TOKEN);
		String accessTokenValue = generateToken(authentication.getName(), claims,
				jwtProperties.getExpireTime().getSeconds());
		AccessToken accessToken = new AccessToken();
		accessToken.setExpiresIn(jwtProperties.getExpireTime().getSeconds());
		accessToken.setValue(jwtProperties.getTokenHead() + accessTokenValue);
		accessToken.setScope(ROLE_TOKEN);
		accessToken.setRefreshToken(generateRefreshToken(accessTokenValue));
		return accessToken;
	}

	/**
	 * 生成RefreshToken
	 *
	 * @param accessToken
	 * @return com.rainsoil.fastjava.security.security.token.RefreshToken
	 * @since 2021/10/10
	 */
	protected RefreshToken generateRefreshToken(String accessToken) {
		Assert.notBlank(accessToken, "accessToken cannot be empty");
		Map<String, Object> claims = new HashMap<>(2);
		claims.put(CLAIM_KEY_AUTHORITIES, ROLE_REFRESH_TOKEN);
		String refreshTokenValue = generateToken(accessToken, claims,
				jwtProperties.getRefreshTokenExpireTime().getSeconds());
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setExpiresIn(jwtProperties.getRefreshTokenExpireTime().getSeconds());
		refreshToken.setValue(jwtProperties.getTokenHead() + refreshTokenValue);
		return refreshToken;
	}

	/**
	 * 移除token
	 *
	 * @param token token
	 * @return void
	 * @since 2021/10/10
	 */
	@Override
	public void removeToken(String token) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 刷新token
	 *
	 * @param refreshToken token
	 * @return com.rainsoil.fastjava.security.security.token.AccessToken
	 * @since 2021/10/10
	 */
	@Override
	public AccessToken refreshToken(String refreshToken) {
		Assert.notBlank(refreshToken, "refreshToken cannot be empty");
		Claims claims = getClaimsFromToken(refreshToken);
		if (!checkRefreshToken(claims)) {
			throw new TokenException("refreshToken 不正确");
		}
		String accessToken = claims.getSubject();
		Authentication authentication = readToken(accessToken);
		return generateAccessToken(authentication);
	}

	/**
	 * 校验AccessToken的角色
	 *
	 * @param claims
	 * @return boolean
	 * @since 2021/10/11
	 */
	private boolean checkRefreshToken(Claims claims) {
		if (claims.get(CLAIM_KEY_AUTHORITIES, String.class).equals(ROLE_REFRESH_TOKEN)) {
			return true;
		}
		return checkExpireTime(claims);
	}

	/**
	 * 校验刷新的token
	 *
	 * @param refreshToken
	 * @return boolean
	 * @since 2021/10/10
	 */
	@Override
	public boolean validateRefreshToken(String refreshToken) {
		if (StrUtil.isBlank(refreshToken)) {
			return false;
		}
		Claims claims = getClaimsFromToken(refreshToken);
		return checkRefreshToken(claims);
	}

	/**
	 * 根据token解析Claims
	 *
	 * @param token token
	 * @return io.jsonwebtoken.Claims
	 * @since 2021/10/10
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	/**
	 * 生成token
	 *
	 * @param subject
	 * @param claims
	 * @param expireTime 失效时间
	 * @return java.lang.String
	 * @since 2021/10/10
	 */
	private String generateToken(String subject, Map<String, Object> claims, long expireTime) {
		Assert.notBlank(jwtProperties.getSecret(), "jwt.secret cannot be empty");
		return Jwts.builder().setClaims(claims).setSubject(subject).setId(UUID.randomUUID().toString())
				.setIssuedAt(new Date()).setExpiration(generateExpirationDate(expireTime))
				.compressWith(CompressionCodecs.DEFLATE).signWith(SIGNATURE_ALGORITHM, jwtProperties.getSecret())
				.compact();
	}

	private Date generateExpirationDate(long expiration) {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

}
