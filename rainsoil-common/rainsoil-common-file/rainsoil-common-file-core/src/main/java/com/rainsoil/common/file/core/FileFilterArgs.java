package com.rainsoil.common.file.core;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件过滤
 *
 * @author luyanan
 * @since 2022/2/7
 **/
@Data
public class FileFilterArgs implements Serializable {
	private static final long serialVersionUID = -1304251232330386415L;
	/**
	 * 前缀
	 *
	 * @since 2021/10/13
	 */

	private String prefix;

	/**
	 * 是否递归查找，如果是false,就模拟文件夹结构查找。
	 *
	 * @since 2021/10/13
	 */

	private Boolean recursive;


	/**
	 * 最大条数
	 *
	 * @since 2021/10/13
	 */

	private int size = 10;


	/**
	 * 以哪个文件开始
	 *
	 * @since 2021/10/13
	 */

	private String startAfter;

	/**
	 * 以什么结尾
	 *
	 * @since 2021/10/13
	 */

	private String suffix;
}
