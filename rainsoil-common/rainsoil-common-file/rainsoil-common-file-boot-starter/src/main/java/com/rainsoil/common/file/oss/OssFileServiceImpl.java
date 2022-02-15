package com.rainsoil.common.file.oss;

import cn.hutool.core.io.IoUtil;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.rainsoil.common.file.FileService;
import com.rainsoil.common.file.core.FileFilterArgs;
import com.rainsoil.common.file.core.FileInfoVo;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用oss实现文件上传
 *
 * @author luyanan
 * @since 2022/2/7
 **/
@RequiredArgsConstructor
public class OssFileServiceImpl implements FileService {

	private final OssTemplate ossTemplate;

	private final OssProperties ossProperties;


	@PostConstruct
	public void init() {


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
		ossTemplate.putObject(ossProperties.getBucketName(), path + "/" + fileName, IoUtil.toStream(contents));
		return FileInfoVo.builder().fileName(fileName)
				.size((long) contents.length)
				.relativePath(path + "/" + fileName)
				.path(path)
				.build();
	}

	protected String getFilePath(String path, String fileName) {
		return (ossProperties.getBucketName() + "/" + path + "/" + fileName);
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
		return ossTemplate.getObject(ossProperties.getBucketName(), fileName).getObjectContent();
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
		for (String file : files) {
			ossTemplate.removeObject(ossProperties.getBucketName(), file);
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
		List<S3ObjectSummary> allObjects = ossTemplate.getAllObjectsByPrefix(ossProperties.getBucketName(), fileFilterArgs);
		return allObjects.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
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
		try {
			return ossTemplate.existObject(ossProperties.getBucketName(), filePath);
		} catch (Exception e) {
			return false;
		}
	}
}
