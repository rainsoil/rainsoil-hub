package com.rainsoil.common.file.local;

import com.rainsoil.common.file.FileService;
import com.rainsoil.common.file.config.FileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 本地文件服务自动化配置
 *
 * @author luyanan
 * @since 2022/2/7
 **/
@ConditionalOnProperty(prefix = FileProperties.PREFIX, name = "type", havingValue = "local", matchIfMissing = true)
@Configuration
@EnableConfigurationProperties(LocalFileProperties.class)
public class LocalFileConfiguration {

	@Bean
	public FileService localFileService(LocalFileProperties localFileProperties) {
		return new LocalFileServiceImpl(localFileProperties);
	}
}
