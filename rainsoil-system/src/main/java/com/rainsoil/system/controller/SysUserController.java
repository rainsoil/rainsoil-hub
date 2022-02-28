package com.rainsoil.system.controller;

import cn.hutool.core.io.IoUtil;
import com.alibaba.excel.EasyExcel;
import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.service.ISysPostService;
import com.rainsoil.system.service.ISysRoleService;
import com.rainsoil.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@Api(value = "系统用户")
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController {


	@Autowired
	private ISysUserService userService;


	/**
	 * 用户列列表
	 *
	 * @param requestParams
	 * @return RespEntity
	 * @since 2021/9/30
	 */
	@ApiOperation(value = "用户列列表")
	@PreAuthorize("@ps.hasPermi('system:user:list')")
	@PostMapping("/list")
	@ResponseBody
	public RespEntity list(@RequestBody PageRequestParams<SysUser> requestParams) {
		PageInfo<SysUser> pageInfo = userService.selectUserList(requestParams);
		return RespEntity.success(pageInfo);
	}

	@PreAuthorize("@ps.hasPermi('system:user:export')")
	@PostMapping("/export")
	@ResponseBody
	public RespEntity export(SysUser user) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		List<SysUser> list = userService.list(user);
		EasyExcel.write(outputStream).sheet("用户数据").doWrite(list);
		String fileName = UUID.randomUUID().toString() + ".xlsx";
		// TODO 文件导出
//		String excel = fileTemplate.upload(IoUtil.toStream(outputStream),
//				FileArgs.builder().path("excel")
//						.fileName(fileName)
//						.rename(false).build());
//
//		ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
//		EasyExcel.write(outputStream).sheet("用户数据").doWrite(list);
//		return util.exportExcel(list, "用户数据");

//		return RespEntity.success(excel);
		return null;
	}

	public void export2(SysUser sysUser) {

	}

//
//	@PreAuthorize("@ps.hasPermi('system:user:import')")
//	@PostMapping("/importData")
//	@ResponseBody
//	public RespEntity importData(MultipartFile file, boolean updateSupport) throws Exception {
//		ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
//		List<SysUser> userList = util.importExcel(file.getInputStream());
//		String message = userService.importUser(userList, updateSupport, getSysUser().getLoginName());
//		return RespEntity.success(message);
//	}
//
//	@PreAuthorize("@ps.hasPermi('system:user:view')")
//	@GetMapping("/importTemplate")
//	@ResponseBody
//	public RespEntity importTemplate() {
//		ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
//		return util.importTemplateExcel("用户数据");
//	}


	/**
	 * 新增保存用户
	 *
	 * @param user
	 * @return RespEntity
	 * @since 2021/9/30
	 */
	@ApiOperation(value = "新增保存用户")
	@PreAuthorize("@ps.hasPermi('system:user:add')")
	@PostMapping("/add")
	@ResponseBody
	public RespEntity addSave(@Validated SysUser user) {
		userService.save(user);
		return RespEntity.success();
	}


	/**
	 * 修改保存用户
	 *
	 * @param user
	 * @return RespEntity
	 * @since 2021/9/30
	 */
	@ApiOperation(value = "修改保存用户")
	@PreAuthorize("@ps.hasPermi('system:user:edit')")
	@PostMapping("/edit")
	@ResponseBody
	public RespEntity editSave(@Validated SysUser user) {

		userService.updateById(user);
		return RespEntity.success();
	}


	/**
	 * 修改密码
	 *
	 * @param user
	 * @return RespEntity
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "修改密码")
	@PreAuthorize("@ps.hasPermi('system:user:resetPwd')")
	@PostMapping("/resetPwd")
	@ResponseBody
	public RespEntity resetPwdSave(SysUser user) {

		userService.resetUserPwd(user);
		return RespEntity.success();
	}


	/**
	 * 用户授权角色
	 *
	 * @param userId
	 * @param roleIds
	 * @return RespEntity
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "用户授权角色")
	@PreAuthorize("@ps.hasPermi('system:user:edit')")
	@PostMapping("/authRole/insertAuthRole")
	@ResponseBody
	public RespEntity insertAuthRole(Long userId, Long[] roleIds) {
		userService.insertUserAuth(userId, roleIds);
		return RespEntity.success();
	}

	/**
	 * 删除
	 *
	 * @param ids
	 * @return RespEntity
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "删除")
	@PreAuthorize("@ps.hasPermi('system:user:remove')")
	@PostMapping("/remove")
	@ResponseBody
	public RespEntity remove(String ids) {
		userService.deleteUserByIds(ids);
		return RespEntity.success();
	}

	/**
	 * 校验用户名
	 *
	 * @param user
	 * @return RespEntity
	 * @since 2021/9/30
	 */
	@ApiOperation(value = "校验用户名")
	@PostMapping("/checkLoginNameUnique")
	@ResponseBody
	public RespEntity checkLoginNameUnique(SysUser user) {
		userService.checkLoginNameUnique(user.getLoginName());
		return RespEntity.success();
	}

	/**
	 * 校验手机号码
	 *
	 * @param user
	 * @return RespEntity
	 * @since 2021/9/30
	 */
	@ApiOperation(value = "校验手机号码")
	@PostMapping("/checkPhoneUnique")
	@ResponseBody
	public RespEntity checkPhoneUnique(SysUser user) {
		userService.checkPhoneUnique(user);
		return RespEntity.success();
	}

	/**
	 * 校验email邮箱
	 *
	 * @param user
	 * @return RespEntity
	 * @since 2021/9/30
	 */
	@ApiOperation(value = "校验email邮箱")
	@PostMapping("/checkEmailUnique")
	@ResponseBody
	public RespEntity checkEmailUnique(SysUser user) {
		userService.checkEmailUnique(user);
		return RespEntity.success();
	}

	/**
	 * 用户状态修改
	 *
	 * @param user
	 * @return RespEntity
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "用户状态修改")
	@PreAuthorize("@ps.hasPermi('system:user:edit')")
	@PostMapping("/changeStatus")
	@ResponseBody
	public RespEntity changeStatus(SysUser user) {
		userService.changeStatus(user);
		return RespEntity.success();
	}


	private String prefix = "system/user";


	@Autowired
	private ISysRoleService roleService;

	@Autowired
	private ISysPostService postService;


	@PreAuthorize("@ps.hasPermi('system:user:view')")
	@GetMapping()
	public String user() {
		return prefix + "/user";
	}


	/**
	 * 新增用户
	 *
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/30
	 */
	@ApiOperation(value = "新增用户")
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		mmap.put("roles", roleService.selectRoleAll().stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
		mmap.put("posts", postService.selectPostAll());
		return prefix + "/add";
	}

	/**
	 * 修改用户
	 *
	 * @param userId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/30
	 */
	@ApiOperation(value = " 修改用户")
	@GetMapping("/edit/{userId}")
	public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
		userService.checkUserDataScope(userId);
		List<SysRole> roles = roleService.selectRolesByUserId(userId);
		mmap.put("user", userService.getById(userId));
		mmap.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
		mmap.put("posts", postService.selectPostsByUserId(userId));
		return prefix + "/edit";
	}

	/**
	 * 修改密码
	 *
	 * @param userId 用户id
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/30
	 */
	@ApiOperation(value = "修改密码")
	@PreAuthorize("@ps.hasPermi('system:user:resetPwd')")
	@GetMapping("/resetPwd/{userId}")
	public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
		mmap.put("user", userService.getById(userId));
		return prefix + "/resetPwd";
	}

	/**
	 * 进入授权角色页
	 *
	 * @param userId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/10/1
	 */
	@ApiOperation(value = "进入授权角色页")
	@GetMapping("/authRole/{userId}")
	public String authRole(@PathVariable("userId") Long userId, ModelMap mmap) {
		SysUser user = userService.getById(userId);
		// 获取用户所属的角色列表
		List<SysRole> roles = roleService.selectRolesByUserId(userId);
		mmap.put("user", user);
		mmap.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
		return prefix + "/authRole";
	}
}