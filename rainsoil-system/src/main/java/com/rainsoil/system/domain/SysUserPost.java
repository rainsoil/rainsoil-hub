package com.rainsoil.system.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户和岗位关联 sys_user_post
 *
 * @author ruoyi
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SysUserPost {
	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 岗位ID
	 */
	private Long postId;


}
