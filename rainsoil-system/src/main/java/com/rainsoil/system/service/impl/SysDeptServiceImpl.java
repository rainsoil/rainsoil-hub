package com.rainsoil.system.service.impl;


import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.common.security.core.core.LoginUserServices;
import com.rainsoil.core.constant.UserConstants;
import com.rainsoil.core.datascope.DataScopeHolder;
import com.rainsoil.core.datascope.annotation.DataScope;
import com.rainsoil.core.module.system.vo.SysDept;
import com.rainsoil.core.module.system.vo.SysRole;
import com.rainsoil.core.module.system.vo.SysUser;
import com.rainsoil.core.module.system.vo.Ztree;
import com.rainsoil.core.utils.StringUtil;
import com.rainsoil.system.SystemCode;
import com.rainsoil.system.exception.SystemException;
import com.rainsoil.system.mapper.SysDeptMapper;
import com.rainsoil.system.service.ISysDeptService;
import com.rainsoil.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 部门管理 服务实现
 *
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {
	@Autowired
	private SysDeptMapper deptMapper;


	@Autowired
	private ISysUserService sysUserService;

	/**
	 * 查询部门管理数据
	 *
	 * @param dept 部门信息
	 * @return 部门信息集合
	 */
	@Override
	@DataScope(deptAlias = "d")
	public List<SysDept> selectDeptList(SysDept dept) {
		return deptMapper.selectDeptList(dept, DataScopeHolder.get());
	}

	/**
	 * 查询部门管理树
	 *
	 * @param dept 部门信息
	 * @return 所有部门信息
	 */
	@Override
	@DataScope(deptAlias = "d")
	public List<Ztree> selectDeptTree(SysDept dept) {
		List<SysDept> deptList = list(dept);
		List<Ztree> ztrees = initZtree(deptList);
		return ztrees;
	}

	/**
	 * 查询部门管理树（排除下级）
	 *
	 * @param dept 部门ID
	 * @return 所有部门信息
	 */
	@Override
	@DataScope(deptAlias = "d")
	public List<Ztree> selectDeptTreeExcludeChild(SysDept dept) {
		Long excludeId = dept.getExcludeId();
		List<SysDept> deptList = list(dept);
		Iterator<SysDept> it = deptList.iterator();
		while (it.hasNext()) {
			SysDept d = (SysDept) it.next();
			if (d.getDeptId().intValue() == excludeId
					|| ArrayUtils.contains(StringUtil.split(d.getAncestors(), ","), excludeId + "")) {
				it.remove();
			}
		}
		List<Ztree> ztrees = initZtree(deptList);
		return ztrees;
	}

	/**
	 * 根据角色ID查询部门（数据权限）
	 *
	 * @param role 角色对象
	 * @return 部门列表（数据权限）
	 */
	@Override
	public List<Ztree> roleDeptTreeData(SysRole role) {
		Long roleId = role.getRoleId();
		List<Ztree> ztrees = new ArrayList<Ztree>();
		List<SysDept> deptList = selectDeptList(new SysDept());
		if (StringUtil.isNotNull(roleId)) {
			List<String> roleDeptList = deptMapper.selectRoleDeptTree(roleId);
			ztrees = initZtree(deptList, roleDeptList);
		} else {
			ztrees = initZtree(deptList);
		}
		return ztrees;
	}

	/**
	 * 对象转部门树
	 *
	 * @param deptList 部门列表
	 * @return 树结构列表
	 */
	public List<Ztree> initZtree(List<SysDept> deptList) {
		return initZtree(deptList, null);
	}

	/**
	 * 对象转部门树
	 *
	 * @param deptList     部门列表
	 * @param roleDeptList 角色已存在菜单列表
	 * @return 树结构列表
	 */
	public List<Ztree> initZtree(List<SysDept> deptList, List<String> roleDeptList) {

		List<Ztree> ztrees = new ArrayList<Ztree>();
		boolean isCheck = StringUtil.isNotNull(roleDeptList);
		for (SysDept dept : deptList) {
			if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
				Ztree ztree = new Ztree();
				ztree.setId(dept.getDeptId());
				ztree.setpId(dept.getParentId());
				ztree.setName(dept.getDeptName());
				ztree.setTitle(dept.getDeptName());
				if (isCheck) {
					ztree.setChecked(roleDeptList.contains(dept.getDeptId() + dept.getDeptName()));
				}
				ztrees.add(ztree);
			}
		}
		return ztrees;
	}


	@Override
	public boolean save(SysDept entity) {
		// 校验部门名称不重复
		checkDeptNameUnique(entity);

		SysDept info = getById(entity.getParentId());
		// 如果父节点不为"正常"状态,则不允许新增子节点
		if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
			throw new SystemException(SystemCode.DEPT_STATUS_DISABLE);
		}
		entity.setAncestors(info.getAncestors() + "," + entity.getParentId());

		return super.save(entity);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(SysDept entity) {
		// 检查部门名称是否重复
		checkDeptNameUnique(entity);
		// 校验上级部门不能是自己
		if (entity.getDeptId().equals(entity.getParentId())) {
			throw new SystemException(SystemCode.DEPT_PARENT_OWN, entity.getDeptName());
		}
		//校验是否包含未停用的部门子部门
		if (StringUtil.equals(UserConstants.DEPT_DISABLE, entity.getStatus())
				&&
				selectNormalChildrenDeptById(entity.getDeptId()) > 0) {
			throw new SystemException(SystemCode.DEPT_HAVE_NORMAL_DEPT);
		}
		SysDept newParentDept = getById(entity.getParentId());
		SysDept oldDept = getById(entity.getDeptId());
		if (StringUtil.isNotNull(newParentDept) && StringUtil.isNotNull(oldDept)) {
			String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
			String oldAncestors = oldDept.getAncestors();
			entity.setAncestors(newAncestors);
			updateDeptChildren(entity.getDeptId(), newAncestors, oldAncestors);
		}
		boolean result = updateById(entity);
		if (UserConstants.DEPT_NORMAL.equals(entity.getStatus()) && StringUtil.isNotEmpty(entity.getAncestors())
				&& !StringUtil.equals("0", entity.getAncestors())) {
			// 如果该部门是启用状态，则启用该部门的所有上级部门
			updateParentDeptStatusNormal(entity);
		}
		return result;

	}


	@Override
	public boolean removeById(Serializable id) {
		// 校验是否存在下级部门
		if (count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id)) > 0) {
			throw new SystemException(SystemCode.HAVE_SUN_DEPT);
		}
		// 校验部门下是否存在用户
		if (sysUserService.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getDeptId, id)) > 0) {
			throw new SystemException(SystemCode.DEPT_HAVE_USER);
		}
		return super.removeById(id);
	}

	/**
	 * 修改该部门的父级部门状态
	 *
	 * @param dept 当前部门
	 */
	private void updateParentDeptStatusNormal(SysDept dept) {
		String ancestors = dept.getAncestors();
		Long[] deptIds = Convert.toLongArray(ancestors);
		deptMapper.updateDeptStatusNormal(deptIds);
	}

	/**
	 * 修改子元素关系
	 *
	 * @param deptId       被修改的部门ID
	 * @param newAncestors 新的父ID集合
	 * @param oldAncestors 旧的父ID集合
	 */
	public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
		List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
		for (SysDept child : children) {
			child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
		}
		if (children.size() > 0) {
			deptMapper.updateDeptChildren(children);
		}
	}


	@Override
	public SysDept getById(Serializable id) {
		SysDept sysDept = super.getById(id);
		// 查询父节点
		if (null != sysDept && !sysDept.getParentId().equals(0L)) {
			sysDept.setParentName(super.getById(sysDept.getParentId()).getDeptName());
		}
		return sysDept;
	}

	/**
	 * 根据ID查询所有子部门（正常状态）
	 *
	 * @param deptId 部门ID
	 * @return 子部门数
	 */
	@Override
	public int selectNormalChildrenDeptById(Long deptId) {
		return deptMapper.selectNormalChildrenDeptById(deptId);
	}

	/**
	 * 校验部门名称是否唯一
	 *
	 * @param dept 部门信息
	 * @return 结果
	 */
	@Override
	public void checkDeptNameUnique(SysDept dept) {
		Long deptId = StringUtil.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
		SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
		if (StringUtil.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
			throw new SystemException(SystemCode.DATA_EXIST, dept.getDeptName());
		}
	}

	/**
	 * 校验部门是否有数据权限
	 *
	 * @param deptId 部门id
	 */
	@Override
	public void checkDeptDataScope(Long deptId) {
		if (!SysUser.isAdmin(Long.valueOf(LoginUserServices.getUserId()))) {
			SysDept dept = new SysDept();
			dept.setDeptId(deptId);
			List<SysDept> depts = list(dept);
			if (StringUtil.isEmpty(depts)) {
				throw new SystemException(SystemCode.BUSINESS_UNAUTHORIZED);
			}
		}
	}
}
