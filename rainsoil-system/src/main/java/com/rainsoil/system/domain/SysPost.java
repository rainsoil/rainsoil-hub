package com.rainsoil.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rainsoil.core.core.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 岗位表 sys_post
 *
 * @author ruoyi
 */
@Data
public class SysPost extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 岗位序号
	 */
	@TableId(type = IdType.AUTO)
	private Long postId;

	/**
	 * 岗位编码
	 */
	@NotBlank(message = "岗位编码不能为空")
	@Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
	@TableField(condition = SqlCondition.LIKE)
	private String postCode;

	/**
	 * 岗位名称
	 */
	@TableField(condition = SqlCondition.LIKE)
	@NotBlank(message = "岗位名称不能为空")
	@Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
	private String postName;

	/**
	 * 岗位排序
	 */
	@NotBlank(message = "显示顺序不能为空")
	private String postSort;

	/**
	 * 状态（0正常 1停用）
	 */
	private String status;

	/**
	 * 用户是否存在此岗位标识 默认不存在
	 */
	@TableField(exist = false)
	private boolean flag = false;


}
