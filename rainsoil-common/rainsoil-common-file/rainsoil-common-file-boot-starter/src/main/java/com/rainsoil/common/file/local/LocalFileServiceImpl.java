package com.rainsoil.common.file.local;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import com.rainsoil.common.file.FileService;
import com.rainsoil.common.file.core.FileFilterArgs;
import com.rainsoil.common.file.core.FileInfoVo;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地文件服务实现
 *
 * @author luyanan
 * @since 2022/2/7
 **/
@RequiredArgsConstructor
public class LocalFileServiceImpl implements FileService {

	private final LocalFileProperties localFileProperties;


	/**
	 * 数据存放父目录
	 *
	 * @since 2022/2/7
	 */

	private String parentPath;

	@PostConstruct
	public void init() {
		parentPath = localFileProperties.getData();
		Assert.notBlank(parentPath, "[" + LocalFileProperties.PREFIX + ".data]配置不能为空");
	}

	/**
	 * 文件上传
	 *
	 * @param contents 文件字节
	 * @param fileName 文件名称
	 * @param path     文件路径
	 * @return com.rainsoil.fastjava.file.core.FileInfoVo
	 * @since 2022/2/7
	 */
	@Override
	public FileInfoVo upload(byte[] contents, String fileName, String path) {
		FileUtil.writeBytes(contents, parentPath + "/" + path + "/" + fileName);
		return FileInfoVo
				.builder()
				.fileName(fileName)
				.path(path)
				.relativePath(path + "/" + fileName)
				.size((long) contents.length)
				.build();
	}

	/**
	 * 查看文件
	 *
	 * @param fileName 文件名
	 * @return java.io.InputStream
	 * @since 2022/2/7
	 */
	@Override
	public InputStream info(String fileName) {
		String filePath = parentPath + "/" + fileName;
		// 检查文件是否存在
		if (!FileUtil.exist(filePath)) {
			return null;
		}
		byte[] bytes = FileUtil.readBytes(parentPath + "/" + fileName);
		return IoUtil.toStream(bytes);
	}

	/**
	 * 文件移除
	 *
	 * @param files 文件
	 * @return boolean
	 * @since 2022/2/7
	 */
	@Override
	public boolean removeFiles(String... files) {
		if (files.length == 0) {
			return false;
		}
		for (String file : files) {
			FileUtil.del(parentPath + "/" + file);
		}
		return true;
	}

	/**
	 * 列出目录下的文件
	 *
	 * @param path           路径
	 * @param fileFilterArgs 文件过滤
	 * @return java.util.List<java.lang.String>
	 * @since 2022/2/7
	 */
	@Override
	public List<String> loopFiles(String path, FileFilterArgs fileFilterArgs) {
		return FileUtil.loopFiles(parentPath + "/" + path, new FileFilter() {
			@Override
			public boolean accept(File pathname) {

				return true;
//				return pathname.getName().startsWith() fileFilterArgs.getPrefix();
			}
		}).stream().map(a -> path + "/" + a.getName()).collect(Collectors.toList());
	}

	/**
	 * 检验文件是否存在
	 *
	 * @param filePath
	 * @return boolean
	 * @since 2022/2/7
	 */
	@Override
	public boolean exist(String filePath) {
		return FileUtil.exist(parentPath + "/" + filePath);
	}
}
