package com.rainsoil.common.file;


import com.rainsoil.common.file.core.FileVo;

/**
 * 文件处理策略类
 *
 * @author luyanan
 * @since 2022/2/7
 **/
public interface FileHandlerStrategy {


	/**
	 * 文件上传
	 *
	 * @param fileVo 文件信息
	 * @since 2022/2/7
	 */
	void upload(FileVo fileVo);


	/**
	 * 查看文件
	 *
	 * @param fileName 文件名
	 * @since 2022/2/7
	 */
	void info(String fileName);

	/**
	 * 文件移除
	 *
	 * @param files 文件
	 * @since 2022/2/7
	 */
	void removeFiles(String... files);
}
