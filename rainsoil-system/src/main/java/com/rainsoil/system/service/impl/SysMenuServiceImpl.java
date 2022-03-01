package com.rainsoil.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.core.module.system.vo.SysMenu;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.module.system.vo.Ztree;
import com.rainsoil.core.utils.StringUtil;
import com.rainsoil.system.SystemCode;
import com.rainsoil.system.domain.SysRoleMenu;
import com.rainsoil.system.domain.SysUserRole;
import com.rainsoil.system.exception.SystemException;
import com.rainsoil.system.mapper.SysMenuMapper;
import com.rainsoil.system.mapper.SysRoleMenuMapper;
import com.rainsoil.system.service.ISysMenuService;
import com.rainsoil.system.service.ISysRoleMenuService;
import com.rainsoil.system.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
	public static final String PREMISSION_STRING = "perms[\"{0}\"]";

	@Autowired
	private SysMenuMapper menuMapper;

	@Autowired
	private SysRoleMenuMapper roleMenuMapper;


	@Autowired
	private ISysRoleMenuService sysRoleMenuService;

	@Autowired
	private ISysUserRoleService sysUserRoleService;

	/**
	 * 根据用户查询菜单
	 *
	 * @param user 用户信息
	 * @return 菜单列表
	 */
	@Override
	public List<SysMenu> selectMenusByUser(SysUser user) {
		List<SysMenu> menus;
		// 管理员显示所有菜单信息
		if (user.isAdmin()) {
			menus = menuMapper.selectMenuNormalAll();
		} else {
			menus = menuMapper.selectMenusByUserId(user.getUserId());
		}
		return getChildPerms(menus, 0);
	}

	/**
	 * 查询菜单集合
	 *
	 * @return 所有菜单信息
	 */
	@Override
	public List<SysMenu> selectMenuList(SysMenu menu, Long userId) {
		List<SysMenu> menuList ;
		if (SysUser.isAdmin(userId)) {
			menuList = list(menu);
		} else {
			menu.getParams().put("userId", userId);
			menuList = menuMapper.selectMenuListByUserId(menu);
		}
		return menuList;
	}

	/**
	 * 查询菜单集合
	 *
	 * @return 所有菜单信息
	 */
	@Override
	public List<SysMenu> selectMenuAll(Long userId) {
		List<SysMenu> menuList = null;
		if (SysUser.isAdmin(userId)) {
			menuList = list();
		} else {
			menuList = this.selectMenuAllByUserId(userId);
		}
		return menuList;
	}

	/**
	 * 根据用户ID查询权限
	 *
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	@Override
	public Set<String> selectPermsByUserId(Long userId) {
		List<String> perms = menuMapper.selectPermsByUserId(userId);
		Set<String> permsSet = new HashSet<>();
		for (String perm : perms) {
			if (StringUtil.isNotEmpty(perm)) {
				permsSet.addAll(Arrays.asList(perm.trim().split(",")));
			}
		}
		return permsSet;
	}

	/**
	 * 根据角色ID查询菜单
	 *
	 * @param role 角色对象
	 * @return 菜单列表
	 */
	@Override
	public List<Ztree> roleMenuTreeData(SysRole role, Long userId) {
		Long roleId = role.getRoleId();
		List<Ztree> ztrees;
		List<SysMenu> menuList = selectMenuAll(userId);
		if (StringUtil.isNotNull(roleId)) {
			List<String> roleMenuList = menuMapper.selectMenuTree(roleId);
			ztrees = initZtree(menuList, roleMenuList, true);
		} else {
			ztrees = initZtree(menuList, null, true);
		}
		return ztrees;
	}

	/**
	 * 查询所有菜单
	 *
	 * @return 菜单列表
	 */
	@Override
	public List<Ztree> menuTreeData(Long userId) {
		List<SysMenu> menuList = selectMenuAll(userId);
		List<Ztree> ztrees = initZtree(menuList);
		return ztrees;
	}

	/**
	 * 查询系统所有权限
	 *
	 * @return 权限列表
	 */
	@Override
	public LinkedHashMap<String, String> selectPermsAll(Long userId) {
		LinkedHashMap<String, String> section = new LinkedHashMap<>();
		List<SysMenu> permissions = selectMenuAll(userId);
		if (StringUtil.isNotEmpty(permissions)) {
			for (SysMenu menu : permissions) {
				section.put(menu.getUrl(), MessageFormat.format(PREMISSION_STRING, menu.getPerms()));
			}
		}
		return section;
	}

	/**
	 * 对象转菜单树
	 *
	 * @param menuList 菜单列表
	 * @return 树结构列表
	 */
	public List<Ztree> initZtree(List<SysMenu> menuList) {
		return initZtree(menuList, null, false);
	}

	/**
	 * 对象转菜单树
	 *
	 * @param menuList     菜单列表
	 * @param roleMenuList 角色已存在菜单列表
	 * @param permsFlag    是否需要显示权限标识
	 * @return 树结构列表
	 */
	public List<Ztree> initZtree(List<SysMenu> menuList, @Null List<String> roleMenuList, boolean permsFlag) {
		List<Ztree> ztrees = new ArrayList<Ztree>();
		boolean isCheck = StringUtil.isNotNull(roleMenuList);
		for (SysMenu menu : menuList) {
			Ztree ztree = new Ztree();
			ztree.setId(menu.getMenuId());
			ztree.setpId(menu.getParentId());
			ztree.setName(transMenuName(menu, permsFlag));
			ztree.setTitle(menu.getMenuName());
			if (isCheck) {
				ztree.setChecked(roleMenuList.contains(menu.getMenuId() + menu.getPerms()));
			}
			ztrees.add(ztree);
		}
		return ztrees;
	}

	public String transMenuName(SysMenu menu, boolean permsFlag) {
		StringBuffer sb = new StringBuffer();
		sb.append(menu.getMenuName());
		if (permsFlag) {
			sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;" + menu.getPerms() + "</font>");
		}
		return sb.toString();
	}

	/**
	 * 删除菜单管理信息
	 *
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	@Override
	public int deleteMenuById(Long menuId) {
		return menuMapper.deleteMenuById(menuId);
	}


	@Override
	public boolean removeById(Serializable id) {
		// 校验是否存在子菜单
		if (count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id)) > 0) {
			throw new SystemException(SystemCode.MENU_HAVE_SUN_MENU);
		}
		// 菜单是否已经分配
		if (sysRoleMenuService.count(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, id)) > 0) {
			throw new SystemException(SystemCode.MENU_HAVE_ROLE);
		}
//		AuthorizationUtils.clearAllCachedAuthorizationInfo();
		return super.removeById(id);
	}


	@Override
	public SysMenu getById(Serializable id) {
		SysMenu sysMenu = super.getById(id);
		if (null != sysMenu) {
			if (null != sysMenu.getParentId() && sysMenu.getParentId() > 0) {
				SysMenu parentMenu = super.getById(sysMenu.getParentId());
				sysMenu.setParentName(parentMenu.getParentName());
			} else {
				sysMenu.setParentName("主目录");
			}
		}
		return sysMenu;
	}


	/**
	 * 查询子菜单数量
	 *
	 * @param parentId 父级菜单ID
	 * @return 结果
	 */
	@Override
	public int selectCountMenuByParentId(Long parentId) {
		return menuMapper.selectCountMenuByParentId(parentId);
	}

	/**
	 * 查询菜单使用数量
	 *
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	@Override
	public int selectCountRoleMenuByMenuId(Long menuId) {
		return roleMenuMapper.selectCountRoleMenuByMenuId(menuId);
	}

	/**
	 * 新增保存菜单信息
	 *
	 * @param menu 菜单信息
	 * @return 结果
	 */
	@Override
	public int insertMenu(SysMenu menu) {
		return menuMapper.insertMenu(menu);
	}


	@Override
	public boolean save(SysMenu entity) {
		checkMenuNameUnique(entity);
		return super.save(entity);
	}


	@Override
	public boolean updateById(SysMenu entity) {
		checkMenuNameUnique(entity);
		return super.updateById(entity);
	}

	/**
	 * 校验菜单名称是否唯一
	 *
	 * @param menu 菜单信息
	 * @return 结果
	 */
	@Override
	public void checkMenuNameUnique(SysMenu menu) {
		Long menuId = StringUtil.isNull(menu.getMenuId()) ? Long.valueOf(-1L) : menu.getMenuId();
		SysMenu info = getOne(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getMenuName, menu.getMenuName())
				.eq(SysMenu::getParentId, menu.getParentId()));
		if (StringUtil.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
			throw new SystemException(SystemCode.DATA_EXIST);
		}
	}

	@Override
	public List<SysMenu> selectMenuAllByUserId(Long userId) {
		// 1. 查询用户关联的角色
		List<Long> roleIds = sysUserRoleService.list(SysUserRole.builder().userId(userId).build()).stream().map(SysUserRole::getRoleId).distinct().collect(Collectors.toList());
		if (CollectionUtil.isEmpty(roleIds)) {
			return new ArrayList<>();
		}
		// 2. 根据角色查询菜单
		List<Long> menuIds = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, roleIds)).stream().map(SysRoleMenu::getMenuId).distinct().collect(Collectors.toList());
		return super.listByIds(menuIds);
	}

	/**
	 * 根据父节点的ID获取所有子节点
	 *
	 * @param list     分类表
	 * @param parentId 传入的父节点ID
	 * @return String
	 */
	public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
		List<SysMenu> returnList = new ArrayList<SysMenu>();
		for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
			SysMenu t = (SysMenu) iterator.next();
			// 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
			if (t.getParentId() == parentId) {
				recursionFn(list, t);
				returnList.add(t);
			}
		}
		return returnList;
	}

	/**
	 * 递归列表
	 *
	 * @param list
	 * @param t
	 */
	private void recursionFn(List<SysMenu> list, SysMenu t) {
		// 得到子节点列表
		List<SysMenu> childList = getChildList(list, t);
		t.setChildren(childList);
		for (SysMenu tChild : childList) {
			if (hasChild(list, tChild)) {
				recursionFn(list, tChild);
			}
		}
	}

	/**
	 * 得到子节点列表
	 */
	private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
		List<SysMenu> tlist = new ArrayList<SysMenu>();
		Iterator<SysMenu> it = list.iterator();
		while (it.hasNext()) {
			SysMenu n = (SysMenu) it.next();
			if (n.getParentId().longValue() == t.getMenuId().longValue()) {
				tlist.add(n);
			}
		}
		return tlist;
	}

	/**
	 * 判断是否有子节点
	 */
	private boolean hasChild(List<SysMenu> list, SysMenu t) {
		return getChildList(list, t).size() > 0 ? true : false;
	}
}
