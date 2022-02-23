package com.rainsoil.system.controller;


import cn.hutool.core.util.ObjectUtil;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.common.security.core.core.LoginUserServices;
import com.rainsoil.core.module.system.vo.SysDept;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.module.system.vo.Ztree;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.service.ISysDeptService;
import com.rainsoil.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门信息
 *
 * @author ruoyi
 */
@Api(value = "系统部门")
@Controller
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
	private String prefix = "system/dept";

	@Autowired
	private ISysDeptService deptService;

	@Autowired
	private ISysUserService sysUserService;

	@PreAuthorize("@ss.hasPermi('system:dept:list')")
	@PostMapping("/list")
	@ResponseBody
	public List<SysDept> list(SysDept dept) {
		List<SysDept> deptList = deptService.selectDeptList(dept);

		return deptList;
	}


	/**
	 * 新增保存部门
	 *
	 * @param dept
	 * @return RespEntity
	 * @since 2021/9/25
	 */
	@PreAuthorize("@ss.hasPermi('system:dept:add')")
	@PostMapping("/add")
	@ResponseBody
	public RespEntity addSave(@Validated SysDept dept) {
		deptService.save(dept);
		return RespEntity.success();
	}


	/**
	 * 修改
	 *
	 * @param dept
	 * @return RespEntity
	 * @since 2021/9/25
	 */
	@ApiOperation(value = " 修改")
	@PreAuthorize("@ss.hasPermi('system:dept:edit')")
	@PostMapping("/edit")
	@ResponseBody
	public RespEntity editSave(@Validated SysDept dept) {
		deptService.updateById(dept);
		return RespEntity.success();
	}

	/**
	 * 删除
	 *
	 * @param deptId
	 * @return RespEntity
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "删除")
	@PreAuthorize("@ss.hasPermi('system:dept:remove')")
	@GetMapping("/remove/{deptId}")
	@ResponseBody
	public RespEntity remove(@PathVariable("deptId") Long deptId) {
		deptService.removeById(deptId);
		return RespEntity.success();
	}

	/**
	 * 校验部门名称
	 *
	 * @param dept
	 * @return RespEntity
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "校验部门名称")
	@PostMapping("/checkDeptNameUnique")
	@ResponseBody
	public RespEntity checkDeptNameUnique(SysDept dept) {
		deptService.checkDeptNameUnique(dept);
		return RespEntity.success();
	}


	/**
	 * 加载部门列表树
	 */
	@GetMapping("/treeData")
	@ResponseBody
	public List<Ztree> treeData() {
		List<Ztree> ztrees = deptService.selectDeptTree(new SysDept());
		return ztrees;
	}

	/**
	 * 加载部门列表树（排除下级）
	 */
	@GetMapping("/treeData/{excludeId}")
	@ResponseBody
	public List<Ztree> treeDataExcludeChild(@PathVariable(value = "excludeId", required = false) Long excludeId) {
		SysDept dept = new SysDept();
		dept.setExcludeId(excludeId);
		List<Ztree> ztrees = deptService.selectDeptTreeExcludeChild(dept);
		return ztrees;
	}

	/**
	 * 加载角色部门（数据权限）列表树
	 */
	@GetMapping("/roleDeptTreeData")
	@ResponseBody
	public List<Ztree> deptTreeData(SysRole role) {
		List<Ztree> ztrees = deptService.roleDeptTreeData(role);
		return ztrees;
	}


	@PreAuthorize("@ss.hasPermi('system:dept:view')")
	@GetMapping()
	public String dept() {
		return prefix + "/dept";
	}


	/**
	 * 新增部门
	 */
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
		if (!SysUser.isAdmin(LoginUserServices.getUserId())) {
			SysUser sysUser = sysUserService.getById(Long.valueOf(LoginUserServices.getUserId()));
			parentId = sysUser.getDeptId();
		}
		mmap.put("dept", deptService.getById(parentId));
		return prefix + "/add";
	}

	/**
	 * 修改
	 *
	 * @param deptId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/25
	 */
	@GetMapping("/edit/{deptId}")
	public String edit(@PathVariable("deptId") Long deptId, ModelMap mmap) {
		deptService.checkDeptDataScope(deptId);
		SysDept dept = deptService.getById(deptId);
		if (ObjectUtil.isNotEmpty(dept) && 100L == deptId) {
			dept.setParentName("无");
		}
		mmap.put("dept", dept);
		return prefix + "/edit";
	}

	/**
	 * 选择部门树
	 *
	 * @param deptId    部门ID
	 * @param excludeId 排除ID
	 */
	@GetMapping(value = {"/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}"})
	public String selectDeptTree(@PathVariable("deptId") Long deptId,
								 @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
		mmap.put("dept", deptService.getById(deptId));
		mmap.put("excludeId", excludeId);
		return prefix + "/tree";
	}

}
