package com.rainsoil.common.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.rainsoil.common.core.constants.Constants;
import com.rainsoil.common.core.constants.SymbolConstants;
import com.rainsoil.common.file.config.FileProperties;
import com.rainsoil.common.file.core.FileFilterArgs;
import com.rainsoil.common.file.core.FileInfoVo;
import com.rainsoil.common.file.core.FileVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件处理模板类
 *
 * @author luyanan
 * @since 2022/2/6
 **/
@AllArgsConstructor
@Slf4j
public class FileTemplate {

	private final FileProperties fileProperties;

	private final FileService fileService;

	private final ObjectProvider<FileHandlerStrategy> fileHandlerStrategies;


	/**
	 * 将文件的相对路径补充为全路径(支持多个文件,多个文件路径用,隔开)
	 *
	 * @param path 路径
	 * @return java.lang.String
	 * @since 2021/10/14
	 */
	public String addHost(String path) {
		Assert.notBlank(fileProperties.getDomain(), "[" + FileProperties.PREFIX + ".domain]配置不能为空");
		if (StrUtil.isBlank(path)) {
			return path;
		}
		return Arrays.stream(path.split(SymbolConstants.COMMA)).filter(a -> StrUtil.isNotBlank(a))
				.map(p -> {
					if (p.startsWith(Constants.HTTP) || p.startsWith(Constants.HTTPS)) {
						return p;
					}
					return URLUtil.normalize((fileProperties.getDomain() + "/" + p).replace("/+", "/"), true, true);
				}).collect(Collectors.joining(SymbolConstants.COMMA));
	}

	/**
	 * 给路径添加前缀
	 *
	 * @param paths
	 * @return java.util.Map<java.lang.String, java.lang.String>
	 * @since 2021/10/14
	 */
	public Map<String, String> addHost(List<String> paths) {
		return Optional
				.ofNullable(paths)
				.filter(a -> CollectionUtil.isNotEmpty(a)).map(ps -> {
					if (CollectionUtil.isEmpty(ps)) {
						return new HashMap<>(0);
					}
					Map<String, String> pathMap = new HashMap<>(paths.size());
					ps.forEach(p -> {
						String absolutePath = null;
						if (p.startsWith(Constants.HTTP) || p.startsWith(Constants.HTTPS)) {
							absolutePath = p;
						} else {
							absolutePath = fileProperties.getDomain() + "/" + p;
						}
						pathMap.put(p, URLUtil.normalize(absolutePath, true, true));
					});
					return pathMap;
				}).orElse(new HashMap(0));
	}

	/**
	 * 移除文件的host为相对路径(支持多个文件,多个文件路径用,隔开)
	 *
	 * @param path 文件全路径
	 * @return java.lang.String
	 * @since 2021/10/15
	 */
	public String removeHost(String path) {
		if (StrUtil.isBlank(path)) {
			return path;
		}
		return Arrays
				.stream(path.split(SymbolConstants.COMMA))
				.map(a -> a.replace(fileProperties.getDomain(), ""))
				.collect(Collectors.joining(SymbolConstants.COMMA));

	}


	/**
	 * 移除文件的host为相对路径(支持多个文件,多个文件路径用,隔开)
	 *
	 * @param paths
	 * @return java.util.Map<java.lang.String, java.lang.String>
	 * @since 2021/10/15
	 */
	public Map<String, String> removeHost(List<String> paths) {
		return Optional
				.ofNullable(paths)
				.filter(a -> CollectionUtil.isNotEmpty(a)).map(ps -> {
					if (CollectionUtil.isEmpty(ps)) {
						return new HashMap<>(0);
					}
					Map<String, String> pathMap = new HashMap<>(paths.size());
					ps.stream().filter(a -> StrUtil.isNotBlank(a)).forEach(p -> {
						String relativePath = p.replace(fileProperties.getDomain(), "");
						pathMap.put(p, relativePath);
					});
					return pathMap;
				}).orElse(new HashMap(0));
	}


	/**
	 * 文件上传
	 *
	 * @param contents 文件字节
	 * @param fileName 文件名称
	 * @param path     文件路径
	 * @param rename   是否重命名
	 * @return com.rainsoil.fastjava.file.core.FileInfoVo
	 * @since 2022/2/7
	 */
	public FileInfoVo upload(byte[] contents, String fileName, String path, boolean rename) {
		FileVo fileVo = new FileVo();
		fileVo.setContents(contents);
		fileVo.setFileName(fileName);
		fileVo.setPath(path);
		fileVo.setRename(rename);
		fileHandlerStrategies.stream().forEachOrdered(f -> {
			f.upload(fileVo);
		});
		FileInfoVo fileInfoVo = fileService.upload(fileVo.getContents(), fileVo.getFileName(), fileVo.getPath());
		fileInfoVo.setFullPath(addHost(fileInfoVo.getRelativePath()));
		return fileInfoVo;
	}


	/**
	 * 查看文件
	 *
	 * @param fileName
	 * @return java.io.InputStream
	 * @since 2022/2/7
	 */
	public InputStream info(String fileName) {
		fileHandlerStrategies.stream().forEachOrdered(f -> {
			f.info(fileName);
		});
		return fileService.info(fileName);
	}


	/**
	 * 文件移除
	 *
	 * @param files 文件
	 * @return boolean
	 * @since 2022/2/7
	 */
	public boolean removeFiles(String... files) {

		fileHandlerStrategies.stream().forEachOrdered(e -> {
			e.removeFiles(files);
		});
		return fileService.removeFiles(files);
	}


	/**
	 * 列出目录下的文件
	 *
	 * @param path           路径
	 * @param fileFilterArgs 文件过滤
	 * @return java.util.List<java.lang.String>
	 * @since 2022/2/7
	 */
	public List<String> loopFiles(String path, FileFilterArgs fileFilterArgs) {
		return fileService.loopFiles(path, fileFilterArgs);
	}
}
