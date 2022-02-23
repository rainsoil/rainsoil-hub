package com.rainsoil.system.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.common.framework.spring.SpringContextHolder;
import com.rainsoil.common.security.core.core.LoginUserService;
import com.rainsoil.common.security.core.core.LoginUserServices;
import com.rainsoil.core.module.system.vo.SysMenu;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.core.utils.StringUtil;
import com.rainsoil.system.SystemCode;
import com.rainsoil.system.config.SystemProperties;
import com.rainsoil.system.service.ISysConfigService;
import com.rainsoil.system.service.ISysMenuService;
import com.rainsoil.system.service.ISysUserService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 首页 业务处理
 *
 * @author ruoyi
 */
@Api(value = "首页业务处理")
@Controller
public class SysIndexController extends BaseController {

	@Autowired
	private ISysUserService sysUserService;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ISysMenuService menuService;

	@Autowired
	private LoginUserService loginUserService;


	@Autowired
	private SystemProperties systemProperties;

	// 解锁屏幕
	@PostMapping("/unlockscreen")
	@ResponseBody
	public RespEntity unlockscreen(String password) {

		loginUserService.lock();
//		SysUser user = sysUserService.getById(Long.valueOf(LoginUserServices.getUserId()));
//		if (ObjectUtil.isNull(user)) {
//			return RespEntity.error(SystemCode.INTERNAL_SERVER_ERROR, "服务器超时，请重新登陆");
//		}
//		if (passwordEncoder.matches(user.getPassword(), password)) {
//			SpringContextHolder.getRequest().getSession().removeAttribute(ShiroConstants.LOCK_SCREEN);
//			return RespEntity.success();
//		}
		return RespEntity.error(SystemCode.INTERNAL_SERVER_ERROR, "密码不正确，请重新输入。");
	}


	// 切换菜单
	@GetMapping("/system/menuStyle/{style}")
	public void menuStyle(@PathVariable String style, HttpServletResponse response) {
		SpringContextHolder.setCookie(response, "nav-style", style, -1);
	}


	// 系统首页
	@GetMapping({"/index", "/"})
	public String index(ModelMap mmap) {
		// 取身份信息
		SysUser user = sysUserService.getById(Long.valueOf(LoginUserServices.getUserId()));
		// 根据用户id取出菜单
		List<SysMenu> menus = menuService.selectMenusByUser(user);
		mmap.put("menus", menus);
		mmap.put("user", user);
		mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
		mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
		mmap.put("ignoreFooter", configService.selectConfigByKey("sys.index.ignoreFooter"));
		mmap.put("copyrightYear", systemProperties.getCopyrightYear());
		mmap.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
		mmap.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));
		mmap.put("isMobile", SpringContextHolder.checkAgentIsMobile(SpringContextHolder.getRequest().getHeader("User-Agent")));

		// 菜单导航显示风格
		String menuStyle = configService.selectConfigByKey("sys.index.menuStyle");
		// 移动端，默认使左侧导航菜单，否则取默认配置
		String indexStyle = SpringContextHolder.checkAgentIsMobile(SpringContextHolder.getRequest().getHeader("User-Agent")) ? "index" : menuStyle;

		// 优先Cookie配置导航菜单
		Cookie[] cookies = SpringContextHolder.getRequest().getCookies();
		for (Cookie cookie : cookies) {
			if (StringUtils.isNotEmpty(cookie.getName()) && "nav-style".equalsIgnoreCase(cookie.getName())) {
				indexStyle = cookie.getValue();
				break;
			}
		}
		String webIndex = "topnav".equalsIgnoreCase(indexStyle) ? "index-topnav" : "index";
		return webIndex;
	}

	// 锁定屏幕
	@GetMapping("/lockscreen")
	public String lockscreen(ModelMap mmap) {
		mmap.put("user", // 取身份信息
				sysUserService.getById(Long.valueOf(LoginUserServices.getUserId())));
//		SpringContextHolder.getRequest().getSession().setAttribute(ShiroConstants.LOCK_SCREEN, true);
		loginUserService.unLock();
		return "lock";
	}

	// 切换主题
	@GetMapping("/system/switchSkin")
	public String switchSkin() {
		return "skin";
	}

	// 系统介绍
	@GetMapping("/system/main")
	public String main(ModelMap mmap) {
		mmap.put("version", systemProperties.getVersion());
//		return "main";
		return "main_v1";
	}


	// 检查初始密码是否提醒修改
	public boolean initPasswordIsModify(Date pwdUpdateDate) {
		Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
		return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
	}

	// 检查密码是否过期
	public boolean passwordIsExpiration(Date pwdUpdateDate) {
		Integer passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
		if (passwordValidateDays != null && passwordValidateDays > 0) {
			if (StringUtil.isNull(pwdUpdateDate)) {
				// 如果从未修改过初始密码，直接提醒过期
				return true;
			}
			Date nowDate = DateUtil.date();
			return DateUtil.betweenDay(nowDate, pwdUpdateDate, false) > passwordValidateDays;
		}
		return false;
	}

}
