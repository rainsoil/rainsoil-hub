package com.rainsoil.system.controller;


import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.SystemCode;
import com.rainsoil.system.service.ISysConfigService;
import com.rainsoil.system.service.SysRegisterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册验证
 *
 * @author ruoyi
 */
@Api(value = "注册")
@Controller
public class SysRegisterController extends BaseController {
	@Autowired
	private SysRegisterService registerService;

	@Autowired
	private ISysConfigService configService;


	@PostMapping("/register")
	@ResponseBody
	public RespEntity ajaxRegister(SysUser user) {
		if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
			return RespEntity.error(SystemCode.INTERNAL_SERVER_ERROR, "当前系统没有开启注册功能！");
		}
		String msg = registerService.register(user);
		return StringUtils.isEmpty(msg) ? RespEntity.success() : RespEntity.error(SystemCode.INTERNAL_SERVER_ERROR, msg);
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}
}
