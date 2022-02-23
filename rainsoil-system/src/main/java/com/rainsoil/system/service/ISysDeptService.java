package com.rainsoil.system.service;


import com.rainsoil.common.data.mybatis.IBaseService;
import com.rainsoil.core.module.system.vo.SysDept;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.Ztree;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author ruoyi
 */
public interface ISysDeptService extends IBaseService<SysDept> {
	/**
	 * 查询部门管理数据
	 *
	 * @param dept 部门信息
	 * @return 部门信息集合
	 */
	 List<SysDept> selectDeptList(SysDept dept);

	/**
	 * 查询部门管理树
	 *
	 * @param dept 部门信息
	 * @return 所有部门信息
	 */
	 List<Ztree> selectDeptTree(SysDept dept);

	/**
	 * 查询部门管理树（排除下级）
	 *
	 * @param dept 部门信息
	 * @return 所有部门信息
	 */
	 List<Ztree> selectDeptTreeExcludeChild(SysDept dept);

	/**
	 * 根据角色ID查询菜单
	 *
	 * @param role 角色对象
	 * @return 菜单列表
	 */
	 List<Ztree> roleDeptTreeData(SysRole role);





	/**
	 * 根据ID查询所有子部门（正常状态）
	 *
	 * @param deptId 部门ID
	 * @return 子部门数
	 */
	public int selectNormalChildrenDeptById(Long deptId);

	/**
	 * 校验部门名称是否唯一
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	void checkDeptNameUnique(SysDept dept);

	/**
	 * 校验部门是否有数据权限
	 *
	 * @param deptId 部门id
	 */
	public void checkDeptDataScope(Long deptId);
}
