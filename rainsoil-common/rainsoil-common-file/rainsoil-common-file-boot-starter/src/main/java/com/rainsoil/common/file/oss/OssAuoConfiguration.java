package com.rainsoil.common.file.oss;

import com.amazonaws.services.s3.AmazonS3;
import com.rainsoil.common.file.FileService;
import com.rainsoil.common.file.config.FileProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * oss自动配置类
 *
 * @author luyanan
 * @since 2021/10/24
 **/
@ConditionalOnClass(AmazonS3.class)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = FileProperties.PREFIX, name = "type", havingValue = "oss", matchIfMissing = false)
//@ConditionalOnProperty(prefix = OssProperties.PREFIX, value = "enable", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(OssProperties.class)
public class OssAuoConfiguration {


	private final OssProperties ossProperties;


	@Bean
	public OssTemplate ossTemplate() {
		return new OssTemplate(ossProperties);
	}


	@Bean
	public FileService ossFileService(OssTemplate ossTemplate, OssProperties ossProperties) {
		return new OssFileServiceImpl(ossTemplate, ossProperties);
	}
}
