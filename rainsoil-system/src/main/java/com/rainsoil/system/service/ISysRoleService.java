package com.rainsoil.system.service;




import com.rainsoil.common.data.mybatis.IBaseService;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.system.domain.SysUserRole;

import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 *
 * @author ruoyi
 */
public interface ISysRoleService extends IBaseService<SysRole> {
	/**
	 * 根据条件分页查询角色数据
	 *
	 * @param role 角色信息
	 * @return 角色数据集合信息
	 */
	 List<SysRole> selectRoleList(SysRole role);

	/**
	 * 根据用户ID查询角色列表
	 *
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	 Set<String> selectRoleKeys(Long userId);

	/**
	 * 根据用户ID查询角色权限
	 *
	 * @param userId 用户ID
	 * @return 角色列表
	 */
	 List<SysRole> selectRolesByUserId(Long userId);


	/**
	 * 根据userId查询角色
	 *
	 * @param userId
	 * @return java.util.List<com.rainsoil.core.module.system.vo.SysRole>
	 * @since 2021/9/29
	 */
	List<SysRole> listByUserId(Long userId);

	/**
	 * 查询所有角色
	 *
	 * @return 角色列表
	 */
	 List<SysRole> selectRoleAll();

	/**
	 * 通过角色ID查询角色
	 *
	 * @param roleId 角色ID
	 * @return 角色对象信息
	 */
	 SysRole selectRoleById(Long roleId);


	/**
	 * 修改数据权限信息
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	 int authDataScope(SysRole role);

	/**
	 * 校验角色名称是否唯一
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	void checkRoleNameUnique(SysRole role);

	/**
	 * 校验角色权限是否唯一
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	void checkRoleKeyUnique(SysRole role);

	/**
	 * 校验角色是否允许操作
	 *
	 * @param role 角色信息
	 */
	 void checkRoleAllowed(SysRole role);

	/**
	 * 校验角色是否有数据权限
	 *
	 * @param roleId 角色id
	 */
	 void checkRoleDataScope(Long roleId);

	/**
	 * 通过角色ID查询角色使用数量
	 *
	 * @param roleId 角色ID
	 * @return 结果
	 */
	 long countUserRoleByRoleId(Long roleId);

	/**
	 * 角色状态修改
	 *
	 * @param role 角色信息
	 * @return 结果
	 */
	 int changeStatus(SysRole role);

	/**
	 * 取消授权用户角色
	 *
	 * @param userRole 用户和角色关联信息
	 * @return 结果
	 */
	 int deleteAuthUser(SysUserRole userRole);

	/**
	 * 批量取消授权用户角色
	 *
	 * @param roleId  角色ID
	 * @param userIds 需要删除的用户数据ID
	 * @return 结果
	 */
	 int deleteAuthUsers(Long roleId, String userIds);

	/**
	 * 批量选择授权用户角色
	 *
	 * @param roleId  角色ID
	 * @param userIds 需要删除的用户数据ID
	 * @return 结果
	 */
	 int insertAuthUsers(Long roleId, String userIds);
}
