package com.rainsoil.system.controller;

import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.common.security.core.core.LoginUserServices;
import com.rainsoil.core.module.system.vo.SysMenu;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.Ztree;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.service.ISysMenuService;
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
 * 菜单信息
 *
 * @author ruoyi
 */
@Api(value = "菜单")
@Controller
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
	private String prefix = "system/menu";
	@Autowired
	private ISysMenuService menuService;


	@PreAuthorize("@ss.hasPermi('system:menu:list')")
	@PostMapping("/list")
	@ResponseBody
	public List<SysMenu> list(SysMenu menu) {
		Long userId = Long.valueOf(LoginUserServices.getUserId());
		List<SysMenu> menuList = menuService.selectMenuList(menu, userId);
		return menuList;
	}

	/**
	 * 删除菜单
	 *
	 * @param menuId
	 * @return RespEntity
	 * @since 2021/9/27
	 */
	@ApiOperation(value = " 删除菜单")
	@PreAuthorize("@ss.hasPermi('system:menu:remove')")
	@GetMapping("/remove/{menuId}")
	@ResponseBody
	public RespEntity remove(@PathVariable("menuId") Long menuId) {
		menuService.removeById(menuId);
		return RespEntity.success();
	}


	/**
	 * 新增保存菜单
	 *
	 * @param menu
	 * @return RespEntity
	 * @since 2021/9/27
	 */
	@ApiOperation(value = "新增保存菜单")
	@PreAuthorize("@ss.hasPermi('system:menu:add')")
	@PostMapping("/add")
	@ResponseBody
	public RespEntity addSave(@Validated SysMenu menu) {
		menuService.save(menu);
		return RespEntity.success();
	}


	/**
	 * 修改保存菜单
	 */
	@PreAuthorize("@ss.hasPermi('system:menu:edit')")
	@PostMapping("/edit")
	@ResponseBody
	public RespEntity editSave(@Validated SysMenu menu) {

		menuService.updateById(menu);
		return RespEntity.success();

	}


	/**
	 * 校验菜单名称
	 *
	 * @param menu
	 * @return RespEntity
	 * @since 2021/9/27
	 */
	@ApiOperation(value = " 校验菜单名称")
	@PostMapping("/checkMenuNameUnique")
	@ResponseBody
	public RespEntity checkMenuNameUnique(SysMenu menu) {
		menuService.checkMenuNameUnique(menu);
		return RespEntity.success();
	}

	/**
	 * 加载角色菜单列表树
	 *
	 * @param role
	 * @return java.util.List<com.rainsoil.core.core.domain.Ztree>
	 * @since 2021/9/27
	 */
	@ApiOperation(value = "加载角色菜单列表树")
	@GetMapping("/roleMenuTreeData")
	@ResponseBody
	public List<Ztree> roleMenuTreeData(SysRole role) {
		Long userId = Long.valueOf(LoginUserServices.getUserId());
		List<Ztree> ztrees = menuService.roleMenuTreeData(role, userId);
		return ztrees;
	}

	/**
	 * 加载所有菜单列表树
	 *
	 * @return java.util.List<com.rainsoil.core.core.domain.Ztree>
	 * @since 2021/9/27
	 */
	@ApiOperation(value = "加载所有菜单列表树")
	@GetMapping("/menuTreeData")
	@ResponseBody
	public List<Ztree> menuTreeData() {
		Long userId = Long.valueOf(LoginUserServices.getUserId());
		List<Ztree> ztrees = menuService.menuTreeData(userId);
		return ztrees;
	}

	@PreAuthorize("@ss.hasPermi('system:menu:view')")
	@GetMapping()
	public String menu() {
		return prefix + "/menu";
	}


	/**
	 * 新增
	 *
	 * @param parentId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/27
	 */
	@ApiOperation(value = " 新增")
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
		SysMenu menu = null;
		if (0L != parentId) {
			menu = menuService.getById(parentId);
		} else {
			menu = new SysMenu();
			menu.setMenuId(0L);
			menu.setMenuName("主目录");
		}
		mmap.put("menu", menu);
		return prefix + "/add";
	}

	/**
	 * 修改菜单
	 *
	 * @param menuId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/27
	 */
	@ApiOperation(value = "修改菜单")
	@GetMapping("/edit/{menuId}")
	public String edit(@PathVariable("menuId") Long menuId, ModelMap mmap) {
		mmap.put("menu", menuService.getById(menuId));
		return prefix + "/edit";
	}

	/**
	 * 选择菜单图标
	 *
	 * @return java.lang.String
	 * @since 2021/9/27
	 */
	@ApiOperation(value = "选择菜单图标")
	@GetMapping("/icon")
	public String icon() {
		return prefix + "/icon";
	}

	/**
	 * 选择菜单树
	 *
	 * @param menuId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/27
	 */
	@ApiOperation(value = "选择菜单树")
	@GetMapping("/selectMenuTree/{menuId}")
	public String selectMenuTree(@PathVariable("menuId") Long menuId, ModelMap mmap) {
		mmap.put("menu", menuService.getById(menuId));
		return prefix + "/tree";
	}

}