package com.rainsoil.common.file.config;


import com.rainsoil.common.file.FileAnnotationHandler;
import com.rainsoil.common.file.FileHandlerStrategy;
import com.rainsoil.common.file.FileService;
import com.rainsoil.common.file.FileTemplate;
import com.rainsoil.common.file.handler.FileNameRenameStrategy;
import com.rainsoil.common.framework.jackson.AnnotationHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件自动化配置
 *
 * @author luyanan
 * @since 2022/2/6
 **/
@ConditionalOnProperty(prefix = FileProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {


	/**
	 * fileTemplate
	 *
	 * @param fileHandlerStrategies 文件处理策略
	 * @param fileProperties        配置文件
	 * @param fileService           文件service
	 * @return com.rainsoil.common.file.FileTemplate
	 * @since 2022/3/6
	 */
	@Bean
	public FileTemplate fileTemplate(ObjectProvider<FileHandlerStrategy> fileHandlerStrategies,

									 FileProperties fileProperties,
									 FileService fileService

	) {
		return new FileTemplate(fileProperties, fileService, fileHandlerStrategies);
	}


	/**
	 * 文件处理
	 *
	 * @return com.rainsoil.common.file.FileHandlerStrategy
	 * @since 2022/3/6
	 */
	@Bean
	public FileHandlerStrategy fileNameRenameStrategy() {
		return new FileNameRenameStrategy();
	}

	/**
	 * 注解处理
	 *
	 * @return com.rainsoil.common.framework.jackson.AnnotationHandler
	 * @since 2022/3/6
	 */
	@Bean("fileAnnotationHandler")
	public AnnotationHandler fileAnnotationHandler() {
		return new FileAnnotationHandler();
	}

}
