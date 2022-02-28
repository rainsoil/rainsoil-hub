package com.rainsoil.system.controller;


import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.domain.SysUserRole;
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

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@Api(value = "角色")
@Controller
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {


	@Autowired
	private ISysRoleService roleService;

	@Autowired
	private ISysUserService userService;


	@ApiOperation(value = "列表")
	@PreAuthorize("@ps.hasPermi('system:role:list')")
	@PostMapping("/list")
	@ResponseBody
	public RespEntity<PageInfo<SysRole>> list(@RequestBody PageRequestParams<SysRole> requestParams) {
		PageInfo<SysRole> pageInfo = roleService.page(requestParams);
		return RespEntity.success(pageInfo);

	}

//	@PreAuthorize("@ps.hasPermi('system:role:export')")
//	@PostMapping("/export")
//	@ResponseBody
//	public RespEntity export(SysRole role) {
//		List<SysRole> list = roleService.selectRoleList(role);
//		ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
//		return util.exportExcel(list, "角色数据");
//	}


	/**
	 * 新增保存角色
	 *
	 * @param role
	 * @return RespEntity
	 * @since 2021/9/28
	 */
	@PreAuthorize("@ps.hasPermi('system:role:add')")
	@PostMapping("/add")
	@ResponseBody
	public RespEntity addSave(@Validated SysRole role) {

		roleService.save(role);
		return RespEntity.success();

	}


	/**
	 * 修改保存角色
	 *
	 * @param role
	 * @return RespEntity
	 * @since 2021/9/28
	 */
	@ApiOperation(value = "修改保存角色")
	@PreAuthorize("@ps.hasPermi('system:role:edit')")
	@PostMapping("/edit")
	@ResponseBody
	public RespEntity editSave(@Validated SysRole role) {
		roleService.updateById(role);
		return RespEntity.success();
	}


	/**
	 * 保存角色分配数据权限
	 *
	 * @param role
	 * @return com.rainsoil.core.core.domain.AjaxResult
	 * @since 2021/9/28
	 */
	@ApiOperation(value = "保存角色分配数据权限")
	@PreAuthorize("@ps.hasPermi('system:role:edit')")
	@PostMapping("/authDataScope")
	@ResponseBody
	public RespEntity authDataScopeSave(SysRole role) {

		roleService.authDataScope(role);
		return RespEntity.success();
	}

	/**
	 * 删除
	 *
	 * @param ids
	 * @return RespEntity
	 * @since 2021/9/28
	 */
	@ApiOperation(value = "删除")
	@PreAuthorize("@ps.hasPermi('system:role:remove')")
	@PostMapping("/remove")
	@ResponseBody
	public RespEntity remove(String ids) {

		roleService.removeByIds(Arrays.stream(ids.split(",")).map(a -> Long.valueOf(a)).collect(Collectors.toList()));
		return RespEntity.success();
	}

	/**
	 * 校验角色名称
	 *
	 * @param role
	 * @return RespEntity
	 * @since 2021/9/28
	 */
	@ApiOperation(value = "校验角色名称")
	@PostMapping("/checkRoleNameUnique")
	@ResponseBody
	public RespEntity checkRoleNameUnique(SysRole role) {
		roleService.checkRoleNameUnique(role);
		return RespEntity.success();
	}

	/**
	 * 校验角色权限
	 *
	 * @param role
	 * @return RespEntity
	 * @since 2021/9/28
	 */
	@ApiOperation(value = "校验角色权限")
	@PostMapping("/checkRoleKeyUnique")
	@ResponseBody
	public RespEntity checkRoleKeyUnique(SysRole role) {
		roleService.checkRoleKeyUnique(role);
		return RespEntity.success();
	}


	/**
	 * 角色状态修改
	 *
	 * @param role
	 * @return RespEntity
	 * @since 2021/9/28
	 */
	@ApiOperation(value = "角色状态修改")
	@PreAuthorize("@ps.hasPermi('system:role:edit')")
	@PostMapping("/changeStatus")
	@ResponseBody
	public RespEntity changeStatus(SysRole role) {
		roleService.changeStatus(role);
		return RespEntity.success();
	}


	/**
	 * 查询已分配用户角色列表
	 *
	 * @param requestParams
	 * @return RespEntity<PageInfo < com.rainsoil.core.module.system.vo.SysUser>>
	 * @since 2021/9/29
	 */
	@ApiOperation(value = "查询已分配用户角色列表")
	@PreAuthorize("@ps.hasPermi('system:role:list')")
	@PostMapping("/authUser/allocatedList")
	@ResponseBody
	public RespEntity<PageInfo<SysUser>> allocatedList(@RequestBody PageRequestParams<SysUser> requestParams) {

		PageInfo<SysUser> pageInfo = userService.selectAllocatedList(requestParams);
		return RespEntity.success(pageInfo);
	}

	/**
	 * 取消授权
	 *
	 * @param userRole
	 * @return RespEntity
	 * @since 2021/9/29
	 */
	@ApiOperation(value = " 取消授权")
	@PreAuthorize("@ps.hasPermi('system:role:edit')")
	@PostMapping("/authUser/cancel")
	@ResponseBody
	public RespEntity cancelAuthUser(SysUserRole userRole) {
		roleService.deleteAuthUser(userRole);
		return RespEntity.success();
	}


	@PreAuthorize("@ps.hasPermi('system:role:edit')")
	@PostMapping("/authUser/cancelAll")
	@ResponseBody
	public RespEntity cancelAuthUserAll(Long roleId, String userIds) {
		roleService.deleteAuthUsers(roleId, userIds);
		return RespEntity.success();
	}


	/**
	 * 查询未分配用户角色列表
	 *
	 * @param requestParams
	 * @return RespEntity<PageInfo < com.rainsoil.core.module.system.vo.SysUser>>
	 * @since 2021/9/29
	 */
	@ApiOperation(value = " 查询未分配用户角色列表")
	@PreAuthorize("@ps.hasPermi('system:role:list')")
	@PostMapping("/authUser/unallocatedList")
	@ResponseBody
	public RespEntity<PageInfo<SysUser>> unallocatedList(@RequestBody PageRequestParams<SysUser> requestParams) {
		PageInfo<SysUser> pageInfo = userService.selectUnallocatedList(requestParams);
		return RespEntity.success(pageInfo);
	}

	/**
	 * 批量选择用户授权
	 */
	@PreAuthorize("@ps.hasPermi('system:role:edit')")
	@PostMapping("/authUser/selectAll")
	@ResponseBody
	public RespEntity selectAuthUserAll(Long roleId, String userIds) {
		roleService.insertAuthUsers(roleId, userIds);
		return RespEntity.success();
	}

	private String prefix = "system/role";


	/**
	 * 跳转列表
	 *
	 * @return java.lang.String
	 * @since 2021/9/28
	 */
	@ApiOperation(value = "跳转列表")
	@PreAuthorize("@ps.hasPermi('system:role:view')")
	@GetMapping()
	public String role() {
		return prefix + "/role";
	}


	/**
	 * 新增角色
	 *
	 * @return java.lang.String
	 * @since 2021/9/28
	 */
	@ApiOperation(value = " 新增角色")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 修改角色
	 *
	 * @param roleId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/28
	 */
	@ApiOperation(value = "修改角色")
	@GetMapping("/edit/{roleId}")
	public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		roleService.checkRoleDataScope(roleId);
		mmap.put("role", roleService.getById(roleId));
		return prefix + "/edit";
	}

	/**
	 * 角色分配数据权限
	 *
	 * @param roleId 角色id
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/28
	 */
	@ApiOperation(value = " 角色分配数据权限")
	@GetMapping("/authDataScope/{roleId}")
	public String authDataScope(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.getById(roleId));
		return prefix + "/dataScope";
	}

	/**
	 * 选择菜单树
	 *
	 * @return java.lang.String
	 * @since 2021/9/28
	 */
	@ApiOperation(value = "选择菜单树")
	@GetMapping("/selectMenuTree")
	public String selectMenuTree() {
		return prefix + "/tree";
	}


	/**
	 * 分配用户
	 *
	 * @param roleId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/28
	 */
	@ApiOperation(value = "分配用户")
	@PreAuthorize("@ps.hasPermi('system:role:edit')")
	@GetMapping("/authUser/{roleId}")
	public String authUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.getById(roleId));
		return prefix + "/authUser";
	}


	/**
	 * 选择用户
	 *
	 * @param roleId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/29
	 */
	@ApiOperation(value = "选择用户")
	@GetMapping("/authUser/selectUser/{roleId}")
	public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.selectRoleById(roleId));
		return prefix + "/selectUser";
	}
}