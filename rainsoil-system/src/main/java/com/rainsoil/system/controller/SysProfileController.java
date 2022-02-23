package com.rainsoil.system.controller;


import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.common.security.core.core.LoginUserServices;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@Slf4j
@Api(value = "个人信息")
@Controller
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {


	@Autowired
	private ISysUserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	/**
	 * 检查密码
	 *
	 * @param password 密码
	 * @return boolean
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "检查密码")
	@GetMapping("/checkPassword")
	@ResponseBody
	public boolean checkPassword(String password) {
		SysUser user = userService.getById(Long.valueOf(LoginUserServices.getUserId()));
		if (passwordEncoder.matches(user.getPassword(), password)) {
			return true;
		}
		return false;
	}


	/**
	 * 修改本人的密码
	 *
	 * @param oldPassword 老密码
	 * @param newPassword 新密码
	 * @return RespEntity
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "修改本人的密码")
	@PostMapping("/resetPwd")
	@ResponseBody
	public RespEntity resetPwd(String oldPassword, String newPassword) {
		SysUser user = userService.getById(Long.valueOf(LoginUserServices.getUserId()));
		userService.resetPwd(user, oldPassword, newPassword);
		return RespEntity.success();
	}


	/**
	 * 修改用户
	 *
	 * @param user
	 * @return RespEntity
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "修改用户")
	@PostMapping("/update")
	@ResponseBody
	public RespEntity update(SysUser user) {
		SysUser currentUser = getSysUser();
		currentUser.setUserName(user.getUserName());
		currentUser.setEmail(user.getEmail());
		currentUser.setPhonenumber(user.getPhonenumber());
		currentUser.setSex(user.getSex());
		userService.updateById(user);
		return RespEntity.success();
	}

	/**
	 * 保存头像
	 *
	 * @param file
	 * @return RespEntity
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "保存头像")
	@SneakyThrows
	@PostMapping("/updateAvatar")
	@ResponseBody
	public RespEntity updateAvatar(@RequestParam("avatarfile") MultipartFile file) {
		SysUser currentUser = getSysUser();
		if (!file.isEmpty()) {
//			String avatar = fileTemplate.upload(file, FileArgs.builder().path("user").build());
//			String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
//			currentUser.setAvatar(avatar);

			// TODO 图片上传
			userService.updateById(currentUser);
		}

		return RespEntity.success();
	}


	private String prefix = "system/user/profile";


	/**
	 * 个人信息
	 *
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "个人信息")
	@GetMapping()
	public String profile(ModelMap mmap) {
		SysUser user = userService.getById(Long.valueOf(LoginUserServices.getUserId()));
		mmap.put("user", user);
		mmap.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
		mmap.put("postGroup", userService.selectUserPostGroup(user.getUserId()));
		return prefix + "/profile";
	}

	/**
	 * 跳转修改密码页面
	 *
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "跳转修改密码页面")
	@GetMapping("/resetPwd")
	public String resetPwd(ModelMap mmap) {
		SysUser user = userService.getById(Long.valueOf(LoginUserServices.getUserId()));
		mmap.put("user", userService.getById(user.getUserId()));
		return prefix + "/resetPwd";
	}

	/**
	 * 修改用户
	 *
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "修改用户")
	@GetMapping("/edit")
	public String edit(ModelMap mmap) {
		SysUser user = userService.getById(Long.valueOf(LoginUserServices.getUserId()));
		mmap.put("user", userService.getById(user.getUserId()));
		return prefix + "/edit";
	}


	/**
	 * 修改头像
	 *
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "修改头像")
	@GetMapping("/avatar")
	public String avatar(ModelMap mmap) {
		SysUser user = getSysUser();
		mmap.put("user", userService.getById(user.getUserId()));
		return prefix + "/avatar";
	}
}
