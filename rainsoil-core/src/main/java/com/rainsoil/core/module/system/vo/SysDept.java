package com.rainsoil.core.module.system.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 部门表 sys_dept
 *
 * @author ruoyi
 */
@Data
public class SysDept implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 部门ID
	 */
	@TableId(type = IdType.AUTO)
	private Long deptId;

	/**
	 * 父部门ID
	 */
	private Long parentId;

	/**
	 * 祖级列表
	 */
	private String ancestors;

	/**
	 * 部门名称
	 */
	@NotBlank(message = "部门名称不能为空")
	@Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
	private String deptName;

	/**
	 * 显示顺序
	 */
	@NotBlank(message = "显示顺序不能为空")

	private String orderNum;

	/**
	 * 负责人
	 */
	private String leader;

	/**
	 * 联系电话
	 */
	@Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
	private String phone;

	/**
	 * 邮箱
	 */
	@Email(message = "邮箱格式不正确")
	@Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
	private String email;

	/**
	 * 部门状态:0正常,1停用
	 */
	private String status;

	/**
	 * 删除标志（0代表存在 2代表删除）
	 */
	private String delFlag;

	/**
	 * 父部门名称
	 */
	@TableField(exist = false)
	private String parentName;

	/**
	 * 排除编号
	 */
	@TableField(exist = false)
	private Long excludeId;
	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
}
