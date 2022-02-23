package com.rainsoil.system.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.common.security.core.core.LoginUserServices;
import com.rainsoil.core.datascope.annotation.DataScope;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.utils.StringUtil;
import com.rainsoil.system.SystemCode;
import com.rainsoil.system.domain.SysRoleDept;
import com.rainsoil.system.domain.SysRoleMenu;
import com.rainsoil.system.domain.SysUserRole;
import com.rainsoil.system.exception.SystemException;
import com.rainsoil.system.mapper.SysRoleDeptMapper;
import com.rainsoil.system.mapper.SysRoleMapper;
import com.rainsoil.system.mapper.SysRoleMenuMapper;
import com.rainsoil.system.service.ISysRoleDeptService;
import com.rainsoil.system.service.ISysRoleMenuService;
import com.rainsoil.system.service.ISysRoleService;
import com.rainsoil.system.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
	@Autowired
	private SysRoleMapper roleMapper;

	@Autowired
	private SysRoleMenuMapper roleMenuMapper;


	@Autowired
	private SysRoleDeptMapper roleDeptMapper;
	@Autowired
	private ISysRoleMenuService sysRoleMenuService;


	@Autowired
	private ISysRoleDeptService sysRoleDeptService;
	@Autowired
	private ISysUserRoleService sysUserRoleService;

	/**
	 * 根据条件分页查询角色数据
	 *
	 * @param role 角色信息
	 * @return 角色数据集合信息
	 */
	@Override
	@DataScope(deptAlias = "d")
	public List<SysRole> selectRoleList(SysRole role) {
		return super.list(role);
	}

	/**
	 * 根据用户ID查询权限
	 *
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	@Override
	public Set<String> selectRoleKeys(Long userId) {
		List<SysRole> perms = listByUserId(userId);
		Set<String> permsSet = new HashSet<>();
		for (SysRole perm : perms) {
			if (StringUtil.isNotNull(perm)) {
				permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
			}
		}
		return permsSet;
	}

	/**
	 * 根据用户ID查询角色
	 *
	 * @param userId 用户ID
	 * @return 角色列表
	 */
	@Override
	public List<SysRole> selectRolesByUserId(Long userId) {
		List<SysRole> userRoles = listByUserId(userId);
		List<SysRole> roles = selectRoleAll();
		for (SysRole role : roles) {
			for (SysRole userRole : userRoles) {
				if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
					role.setFlag(true);
					break;
				}
			}
		}
		return roles;
	}

	@Override
	public List<SysRole> listByUserId(Long userId) {
		// 查询
		List<Long> roleIds = sysUserRoleService.list(SysUserRole.builder().userId(userId).build()).stream().map(SysUserRole::getRoleId).distinct().collect(Collectors.toList());
		if (CollectionUtil.isEmpty(roleIds)) {
			return new ArrayList<>();
		}
		return listByIds(roleIds);
	}

	/**
	 * 查询所有角色
	 *
	 * @return 角色列表
	 */
	@Override
	public List<SysRole> selectRoleAll() {
		return this.selectRoleList(new SysRole());
	}

	/**
	 * 通过角色ID查询角色
	 *
	 * @param roleId 角色ID
	 * @return 角色对象信息
	 */
	@Override
	public SysRole selectRoleById(Long roleId) {
		return roleMapper.selectRoleById(roleId);
	}


	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		List<Long> roleIds = (List<Long>) idList;
		for (Long roleId : roleIds) {

			checkRoleAllowed(new SysRole(roleId));
			SysRole role = getById(roleId);
			if (sysUserRoleService.count(SysUserRole.builder().roleId(roleId).build()) > 0) {
				throw new SystemException(SystemCode.ROLE_HAVE_USER, role.getRoleName());
			}
		}
		// 删除角色与菜单关联
		sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().in(CollectionUtil.isNotEmpty(roleIds), SysRoleMenu::getRoleId, roleIds));
		// 删除角色与部门关联
		sysRoleDeptService.remove(new LambdaQueryWrapper<SysRoleDept>().in(CollectionUtil.isNotEmpty(roleIds), SysRoleDept::getRoleId, roleIds));
		return super.removeByIds(idList);
	}


	@Override
	public boolean save(SysRole entity) {
		// 增加角色信息
		boolean save = super.save(entity);
		insertRoleMenu(entity);
		return save;
	}

	@Override
	public boolean updateById(SysRole entity) {
		this.checkRoleAllowed(entity);
		checkRoleKeyUnique(entity);
		checkRoleNameUnique(entity);
		boolean update = super.updateById(entity);
		// 删除角色和菜单关联
		sysRoleMenuService.remove(SysRoleMenu.builder().roleId(entity.getRoleId()).build());
		insertRoleMenu(entity);
		return update;
	}

	/**
	 * 修改数据权限信息
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	@Transactional
	public int authDataScope(SysRole role) {
		// 校验角色权限
		checkRoleAllowed(role);
		// 修改角色信息
		roleMapper.updateRole(role);
		// 删除角色与部门关联
		roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
		// 新增角色和部门信息（数据权限）
		return insertRoleDept(role);

	}

	/**
	 * 新增角色菜单信息
	 *
	 * @param role 角色对象
	 */
	public int insertRoleMenu(SysRole role) {
		int rows = 1;
		// 新增用户与角色管理
		List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
		for (Long menuId : role.getMenuIds()) {
			SysRoleMenu rm = new SysRoleMenu();
			rm.setRoleId(role.getRoleId());
			rm.setMenuId(menuId);
			list.add(rm);
		}
		if (list.size() > 0) {
			rows = sysRoleMenuService.saveBatch(list) ? 1 : 0;
		}
		return rows;
	}

	/**
	 * 新增角色部门信息(数据权限)
	 *
	 * @param role 角色对象
	 */
	public int insertRoleDept(SysRole role) {
		int rows = 1;
		// 新增角色与部门（数据权限）管理
		List<SysRoleDept> list = new ArrayList<SysRoleDept>();
		for (Long deptId : role.getDeptIds()) {
			SysRoleDept rd = new SysRoleDept();
			rd.setRoleId(role.getRoleId());
			rd.setDeptId(deptId);
			list.add(rd);
		}
		if (list.size() > 0) {
			rows = sysRoleDeptService.saveBatch(list) ? 1 : 0;
		}
		return rows;
	}

	/**
	 * 校验角色名称是否唯一
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	public void checkRoleNameUnique(SysRole role) {
		Long roleId = StringUtil.isNull(role.getRoleId()) ? -1L : role.getRoleId();
		SysRole info = getOne(SysRole.builder().roleName(role.getRoleName()).build());
		if (StringUtil.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
			throw new SystemException(SystemCode.DATA_EXIST, role.getRoleName());
		}
	}

	/**
	 * 校验角色权限是否唯一
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	public void checkRoleKeyUnique(SysRole role) {
		Long roleId = StringUtil.isNull(role.getRoleId()) ? -1L : role.getRoleId();
		SysRole info = super.getOne(SysRole.builder().roleKey(role.getRoleKey()).build());
		if (StringUtil.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
			throw new SystemException(SystemCode.DATA_EXIST, role.getRoleKey());
		}

	}

	/**
	 * 校验角色是否允许操作
	 *
	 * @param role 角色信息
	 */
	@Override
	public void checkRoleAllowed(SysRole role) {
		if (StringUtil.isNotNull(role.getRoleId()) && role.isAdmin()) {
			throw new SystemException(SystemCode.BUSINESS_UNAUTHORIZED);
//			throw new ServiceException("不允许操作超级管理员角色");
		}
	}

	/**
	 * 校验角色是否有数据权限
	 *
	 * @param roleId 角色id
	 */
	@Override
	public void checkRoleDataScope(Long roleId) {
		if (!SysUser.isAdmin(LoginUserServices.getUserId())) {
			SysRole role = new SysRole();
			role.setRoleId(roleId);
			List<SysRole> roles = this.selectRoleList(role);
			if (StringUtil.isEmpty(roles)) {
				throw new SystemException(SystemCode.BUSINESS_UNAUTHORIZED);
			}
		}
	}

	/**
	 * 通过角色ID查询角色使用数量
	 *
	 * @param roleId 角色ID
	 * @return 结果
	 */
	@Override
	public int countUserRoleByRoleId(Long roleId) {
		return sysUserRoleService.count(SysUserRole.builder().roleId(roleId).build());
	}

	/**
	 * 角色状态修改
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	@Override
	public int changeStatus(SysRole role) {
		checkRoleAllowed(role);
		return super.updateById(role) ? 1 : 0;
	}

	/**
	 * 取消授权用户角色
	 *
	 * @param userRole 用户和角色关联信息
	 * @return 结果
	 */
	@Override
	public int deleteAuthUser(SysUserRole userRole) {
		return sysUserRoleService.remove(userRole) ? 1 : 0;
	}

	/**
	 * 批量取消授权用户角色
	 *
	 * @param roleId  角色ID
	 * @param userIds 需要删除的用户数据ID
	 * @return 结果
	 */
	@Override
	public int deleteAuthUsers(Long roleId, String userIds) {
		return sysUserRoleService.remove(roleId,
				Arrays.stream(userIds.split(",")).map(a -> Long.valueOf(a)).collect(Collectors.toList()));
	}

	/**
	 * 批量选择授权用户角色
	 *
	 * @param roleId  角色ID
	 * @param userIds 需要删除的用户数据ID
	 * @return 结果
	 */
	@Override
	public int insertAuthUsers(Long roleId, String userIds) {
		Long[] users = Convert.toLongArray(userIds);
		// 新增用户与角色管理
		List<SysUserRole> list = new ArrayList<SysUserRole>();
		for (Long userId : users) {
			SysUserRole ur = new SysUserRole();
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			list.add(ur);
		}
		return sysUserRoleService.saveBatch(list) ? 1 : 0;
	}
}
