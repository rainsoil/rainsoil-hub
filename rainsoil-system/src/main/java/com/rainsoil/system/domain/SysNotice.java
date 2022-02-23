package com.rainsoil.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.rainsoil.core.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 通知公告表 sys_notice
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName
public class SysNotice extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 公告ID
	 */
	@TableId(type = IdType.AUTO)
	private Long noticeId;

	/**
	 * 公告标题
	 */
	@NotBlank(message = "公告标题不能为空")
	@Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
	@TableField(condition = SqlCondition.LIKE)
	private String noticeTitle;

	/**
	 * 公告类型（1通知 2公告）
	 */
	private String noticeType;

	/**
	 * 公告内容
	 */
	private String noticeContent;

	/**
	 * 公告状态（0正常 1关闭）
	 */
	private String status;


}
