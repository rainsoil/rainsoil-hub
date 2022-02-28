package com.rainsoil.system.controller;

import com.rainsoil.common.framework.spring.SpringContextHolder;
import com.rainsoil.common.security.core.annotation.IgnoringLogin;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.rainsoil.common.security.security.token.AccessToken;
//import com.rainsoil.common.security.security.token.TokenService;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Api(value = "登录相关")
@Controller
public class SysLoginController extends BaseController {



//
//	@Autowired
//	private AuthenticationManager authenticationManager;
//
//	@Autowired
//	private TokenService tokenService;

	/**
	 * 是否开启记住我功能
	 */
	@Value("${shiro.rememberMe.enabled: false}")
	private boolean rememberMe;

	@Autowired
	private ConfigService configService;

//	/**
//	 * 登录
//	 *
//	 * @param username 用户名
//	 * @param password 密码
//	 * @return RespEntity
//	 * @since 2021/10/7
//	 */
//	@IgnoringLogin
//	@ApiOperation(value = "登录")
//	@PostMapping(value = "login")
//	@ResponseBody
//	public RespEntity login(String username, String password, HttpServletResponse response) {
//		Authentication authenticate = authenticate(username, password);
//		AccessToken accessToken = tokenService.getAccessToken(authenticate);
//		//存储认证信息
//		SecurityContextHolder.getContext().setAuthentication(authenticate);
//		SpringContextHolder.setCookie(response, "Authorization", URLUtil.encode(accessToken.getValue()), (int) accessToken.getExpiresIn());
//		SpringContextHolder.setCookie(response, "refreshToken", URLUtil.encode(accessToken.getRefreshToken().getValue()),
//				(int) accessToken.getRefreshToken().getExpiresIn());
//
//		return RespEntity.success(accessToken);
//	}
//
//
//	private Authentication authenticate(String username, String password) {
//		try {
//			//该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
//			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//		} catch (DisabledException | BadCredentialsException e) {
//
//			throw new UsernameNotFoundException("用户名密码错误");
//		}
//	}


	/**
	 * 注销登录
	 *
	 * @param response
	 * @return void
	 * @since 2021/10/12
	 */
	@ApiOperation(value = "注销登录")
	@GetMapping("logout")
	public void logout(HttpServletResponse response) {
		SpringContextHolder.removeCookie(response, "Authorization", null);
		SpringContextHolder.removeCookie(response, "refreshToken", null);
		SecurityContextHolder.clearContext();
	}

	@IgnoringLogin
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap mmap) {
		// 如果是Ajax请求，返回Json字符串。
		if (SpringContextHolder.isAjaxRequest(request)) {
			return SpringContextHolder.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
		}
		// 是否开启记住我
		mmap.put("isRemembered", rememberMe);
		// 是否开启用户注册
		mmap.put("isAllowRegister", configService.getKey("sys.account.registerUser"));
		SpringContextHolder.setCookie(response, "clientId", "web", -1);
		return "login";
	}

	@IgnoringLogin
	@GetMapping("/unauth")
	public String unauth() {
		return "error/unauth";
	}
}
