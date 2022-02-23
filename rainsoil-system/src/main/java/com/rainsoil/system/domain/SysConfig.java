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
 * 参数配置表 sys_config
 *
 * @author ruoyi
 */
@Data
public class SysConfig extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 参数主键
	 */
	@TableId(type = IdType.INPUT)
	private Long configId;

	/**
	 * 参数名称
	 */
	@NotBlank(message = "参数名称不能为空")
	@Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
	@TableField(condition = SqlCondition.LIKE)
	private String configName;

	/**
	 * 参数键名
	 */
	@NotBlank(message = "参数键名长度不能为空")
	@Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
	@TableField(condition = SqlCondition.LIKE)
	private String configKey;

	/**
	 * 参数键值
	 */
	@NotBlank(message = "参数键值不能为空")
	@Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符")
	private String configValue;

	/**
	 * 系统内置（Y是 N否）
	 */
	private String configType;


}
