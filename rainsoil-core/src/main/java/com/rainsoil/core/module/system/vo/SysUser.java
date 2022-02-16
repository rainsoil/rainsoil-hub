package com.rainsoil.core.module.system.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.rainsoil.common.file.core.annotation.FileHostProperty;
import com.rainsoil.core.core.BaseEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
@Builder
@AllArgsConstructor
@Data
@ExcelIgnoreUnannotated
public class SysUser extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@ExcelProperty("用户序号")
	@TableId(type = IdType.AUTO)
	private Long userId;

	/**
	 * 部门ID
	 */
	@ExcelProperty("部门编号")
	private Long deptId;

	/**
	 * 部门父ID
	 */
	@ExcelProperty("部门父ID")
	@TableField(exist = false)
	private Long parentId;

	/**
	 * 角色ID
	 */
	@ExcelProperty("角色ID")
	@TableField(exist = false)
	private Long roleId;

	/**
	 * 登录名称
	 */
	@NotBlank(message = "登录账号不能为空")
	@Size(min = 0, max = 30, message = "登录账号长度不能超过30个字符")
	@ExcelProperty(value = "登录名称")
	private String loginName;

	/**
	 * 用户名称
	 */
	@Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
	@ExcelProperty(value = "用户名称")
	private String userName;

	/**
	 * 用户类型
	 */
	private String userType;

	/**
	 * 用户邮箱
	 */
	@Email(message = "邮箱格式不正确")
	@Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
	@ExcelProperty(value = "用户邮箱")
	private String email;

	/**
	 * 手机号码
	 */
	@Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
	@ExcelProperty(value = "手机号码")
	private String phonenumber;

	/**
	 * 用户性别
	 */
	@ExcelProperty(value = "用户性别")
	private String sex;

	/**
	 * 用户头像
	 */

	@FileHostProperty
	private String avatar;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 盐加密
	 */
	private String salt;

	/**
	 * 帐号状态（0正常 1停用）
	 */
	private String status;

	/**
	 * 删除标志（0代表存在 2代表删除）
	 */
	@TableLogic
	private String delFlag;

	/**
	 * 最后登录IP
	 */
	private String loginIp;

	/**
	 * 最后登录时间
	 */
	private Date loginDate;

	/**
	 * 密码最后更新时间
	 */
	private Date pwdUpdateDate;

	/**
	 * 部门对象
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private SysDept dept;


	@ExcelIgnore
	@TableField(exist = false)
	private List<SysRole> roles;

	/**
	 * 角色组
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private Long[] roleIds;

	/**
	 * 岗位组
	 */
	@ExcelIgnore
	@TableField(exist = false)
	private Long[] postIds;

	public SysUser() {

	}

	public SysUser(Long userId) {
		this.userId = userId;
	}


	public boolean isAdmin() {
		return isAdmin(this.userId);
	}

	public static boolean isAdmin(Long userId) {
		return userId != null && 1L == userId;
	}

	public static boolean isAdmin(String userId) {
		return userId != null && "1".equals(userId);
	}

	public SysDept getDept() {
		if (dept == null) {
			dept = new SysDept();
		}
		return dept;
	}

	public void setDept(SysDept dept) {
		this.dept = dept;
	}

}
