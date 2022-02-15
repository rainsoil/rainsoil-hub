package com.rainsoil.common.file.handler;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.rainsoil.common.file.FileHandlerStrategy;
import com.rainsoil.common.file.core.FileVo;

/**
 * 文件名重命名策略
 *
 * @author luyanan
 * @since 2022/2/7
 **/
public class FileNameRenameStrategy implements FileHandlerStrategy {


	/**
	 * 文件上传
	 *
	 * @param fileVo
	 * @return void 文件内容
	 * @since 2022/2/7
	 */
	@Override
	public void upload(FileVo fileVo) {
		if (fileVo.isRename()) {
			String extName = FileNameUtil.extName(fileVo.getFileName());
			String fileName = SecureUtil.md5(IoUtil.toStream(fileVo.getContents()))
					+ (StrUtil.isNotBlank(extName) ? "." + extName : "");
			fileVo.setFileName(fileName);
		}
	}

	/**
	 * 查看文件
	 *
	 * @param fileName 文件名
	 * @return java.io.InputStream
	 * @since 2022/2/7
	 */
	@Override
	public void info(String fileName) {

	}

	/**
	 * 文件移除
	 *
	 * @param files 文件
	 * @return boolean
	 * @since 2022/2/7
	 */
	@Override
	public void removeFiles(String... files) {

	}
}
