package com.rainsoil.core.module.system.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rainsoil.core.core.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 角色表 sys_role
 *
 * @author ruoyi
 */
@Data
@Builder
@AllArgsConstructor
public class SysRole extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@TableId(type = IdType.AUTO)
	private Long roleId;

	/**
	 * 角色名称
	 */

	@NotBlank(message = "角色名称不能为空")
	@Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
	private String roleName;

	/**
	 * 角色权限
	 */
	@NotBlank(message = "权限字符不能为空")
	@Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
	private String roleKey;

	/**
	 * 角色排序
	 */
	@NotBlank(message = "显示顺序不能为空")
	private String roleSort;

	/**
	 * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
	 */
	private String dataScope;

	/**
	 * 角色状态（0正常 1停用）
	 */
	private String status;

	/**
	 * 删除标志（0代表存在 2代表删除）
	 */
	private String delFlag;

	/**
	 * 用户是否存在此角色标识 默认不存在
	 */
	@TableField(exist = false)
	private boolean flag = false;

	/**
	 * 菜单组
	 */
	@TableField(exist = false)
	private Long[] menuIds;

	/**
	 * 部门组（数据权限）
	 */
	@TableField(exist = false)
	private Long[] deptIds;

	public SysRole() {

	}

	public SysRole(Long roleId) {
		this.roleId = roleId;
	}


	public boolean isAdmin() {
		return isAdmin(this.roleId);
	}

	public static boolean isAdmin(Long roleId) {
		return roleId != null && 1L == roleId;
	}


}
