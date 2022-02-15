package com.rainsoil.common.file.core;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件信息
 *
 * @author luyanan
 * @since 2022/2/6
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileInfoVo {

	/**
	 * 文件名
	 *
	 * @since 2021/12/28
	 */
	@ApiModelProperty(value = "文件名")
	private String fileName;

	/**
	 * 文件大小
	 *
	 * @since 2021/12/28
	 */
	@ApiModelProperty(value = "文件大小")
	private Long size;

	/**
	 * 相对路径
	 *
	 * @since 2021/12/28
	 */
	@ApiModelProperty(value = "相对路径")
	private String relativePath;

	/**
	 * 全路径
	 *
	 * @since 2021/12/28
	 */
	@ApiModelProperty(value = "全路径")
	private String fullPath;

	/**
	 * 路径
	 *
	 * @since 2021/12/28
	 */

	private String path;

}
