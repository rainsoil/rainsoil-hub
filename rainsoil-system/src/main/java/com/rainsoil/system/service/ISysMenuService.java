package com.rainsoil.system.service;



import com.rainsoil.common.data.mybatis.IBaseService;
import com.rainsoil.core.module.system.vo.SysMenu;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.module.system.vo.Ztree;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 菜单 业务层
 *
 * @author ruoyi
 */
public interface ISysMenuService extends IBaseService<SysMenu> {
	/**
	 * 根据用户ID查询菜单
	 *
	 * @param user 用户信息
	 * @return 菜单列表
	 */
	public List<SysMenu> selectMenusByUser(SysUser user);

	/**
	 * 查询系统菜单列表
	 *
	 * @param menu   菜单信息
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	public List<SysMenu> selectMenuList(SysMenu menu, Long userId);

	/**
	 * 查询菜单集合
	 *
	 * @param userId 用户ID
	 * @return 所有菜单信息
	 */
	public List<SysMenu> selectMenuAll(Long userId);

	/**
	 * 根据用户ID查询权限
	 *
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	public Set<String> selectPermsByUserId(Long userId);

	/**
	 * 根据角色ID查询菜单
	 *
	 * @param role   角色对象
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	public List<Ztree> roleMenuTreeData(SysRole role, Long userId);

	/**
	 * 查询所有菜单信息
	 *
	 * @param userId 用户ID
	 * @return 菜单列表
	 */
	public List<Ztree> menuTreeData(Long userId);

	/**
	 * 查询系统所有权限
	 *
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	public Map<String, String> selectPermsAll(Long userId);

	/**
	 * 删除菜单管理信息
	 *
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	public int deleteMenuById(Long menuId);


	/**
	 * 查询菜单数量
	 *
	 * @param parentId 菜单父ID
	 * @return 结果
	 */
	public int selectCountMenuByParentId(Long parentId);

	/**
	 * 查询菜单使用数量
	 *
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	public int selectCountRoleMenuByMenuId(Long menuId);

	/**
	 * 新增保存菜单信息
	 *
	 * @param menu 菜单信息
	 * @return 结果
	 */
	public int insertMenu(SysMenu menu);


	/**
	 * 校验菜单名称是否唯一
	 *
	 * @param menu 菜单信息
	 * @return 结果
	 */
	public void checkMenuNameUnique(SysMenu menu);


	/**
	 * 根据用户id查询菜单列表
	 *
	 * @param userId 用户id
	 * @return java.util.List<com.rainsoil.core.module.system.vo.SysMenu>
	 * @since 2021/9/27
	 */
	List<SysMenu> selectMenuAllByUserId(Long userId);
}
