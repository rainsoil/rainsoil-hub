package com.rainsoil.common.file;


import com.rainsoil.common.file.core.FileFilterArgs;
import com.rainsoil.common.file.core.FileInfoVo;

import java.io.InputStream;
import java.util.List;

/**
 * 文件服务策略
 *
 * @author luyanan
 * @since 2022/2/7
 **/
public interface FileService {


	/**
	 * 文件上传
	 *
	 * @param contents 文件字节
	 * @param fileName 文件名称
	 * @param path     文件路径
	 * @return com.rainsoil.fastjava.file.core.FileInfoVo
	 * @since 2022/2/7
	 */
	FileInfoVo upload(byte[] contents, String fileName, String path);


	/**
	 * 查看文件
	 *
	 * @param fileName 文件名
	 * @return java.io.InputStream
	 * @since 2022/2/7
	 */
	InputStream info(String fileName);

	/**
	 * 文件移除
	 *
	 * @param files 文件
	 * @return boolean
	 * @since 2022/2/7
	 */
	boolean removeFiles(String... files);


	/**
	 * 列出目录下的文件
	 *
	 * @param path           路径
	 * @param fileFilterArgs 文件过滤
	 * @return java.util.List<java.lang.String>
	 * @since 2022/2/7
	 */
	List<String> loopFiles(String path, FileFilterArgs fileFilterArgs);


	/**
	 * 检验文件是否存在
	 *
	 * @param filePath
	 * @return boolean
	 * @since 2022/2/7
	 */
	boolean exist(String filePath);

}
