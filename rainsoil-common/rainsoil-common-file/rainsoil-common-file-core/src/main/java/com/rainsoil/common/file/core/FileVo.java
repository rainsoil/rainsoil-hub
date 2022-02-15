package com.rainsoil.common.file.core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件信息
 *
 * @author luyanan
 * @since 2022/2/7
 **/
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "I prefer to suppress these FindBugs warnings")
@Data
public class FileVo implements Serializable {


	/**
	 * 文件字节数组
	 *
	 * @since 2022/2/7
	 */

	private byte[] contents;
	/**
	 * 文件名
	 *
	 * @since 2022/2/7
	 */

	private String fileName;

	/**
	 * 文件路径
	 *
	 * @since 2022/2/7
	 */

	private String path;

	/**
	 * 是否重命名
	 *
	 * @since 2022/2/7
	 */

	private boolean rename;
}
