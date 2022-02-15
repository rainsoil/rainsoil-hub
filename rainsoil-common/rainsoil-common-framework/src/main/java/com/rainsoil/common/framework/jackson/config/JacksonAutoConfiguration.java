package com.rainsoil.common.framework.jackson.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainsoil.common.framework.jackson.AnnotationHandler;
import com.rainsoil.common.framework.jackson.DefaultAnnotationIntrospector;


import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luyanan
 * @since 2021/10/17
 **/
@Configuration
public class JacksonAutoConfiguration {

	/**
	 * defaultAnnotationIntrospector
	 * @since 2022/2/8
	 * @param objectMapper  objectMapper
	 * @param annotationHandlers  注解处理器
	 * @return com.rainsoil.common.framework.jackson.DefaultAnnotationIntrospector
	 */
	@Bean
	public DefaultAnnotationIntrospector defaultAnnotationIntrospector(ObjectMapper objectMapper,
			ObjectProvider<AnnotationHandler> annotationHandlers) {
		DefaultAnnotationIntrospector introspector = new DefaultAnnotationIntrospector();
		objectMapper.setAnnotationIntrospector(introspector);
		annotationHandlers.stream().forEach(annotationHandler -> {
			introspector.addAnnotationHandler(annotationHandler.annotationClass(), annotationHandler);
		});
		return introspector;
	}

	// @Bean
	// public FileAnnotationHandler dictAnnotationHandler(@Autowired
	// DefaultAnnotationIntrospector introspector, ObjectProvider<AnnotationHandler>
	// annotationHandlers) {
	// FileAnnotationHandler handler = new FileAnnotationHandler();
	//
	// introspector.addAnnotationHandler(AddHost.class, handler);
	// return handler;
	// }

	// @Primary
	// @Bean
	// public ObjectMapper objectMapper(@Autowired DefaultAnnotationIntrospector
	// introspector, ObjectMapper objectMapper) {
	//// //注解扩展json序列化
	//// if (defaultAnnotationIntrospector != null) {
	//// _deserializationConfig =
	// _deserializationConfig.withAppendedAnnotationIntrospector(defaultAnnotationIntrospector);
	//// _serializationConfig =
	// _serializationConfig.withAppendedAnnotationIntrospector(defaultAnnotationIntrospector);
	//// }
	// objectMapper.setAnnotationIntrospector(introspector);
	//// CustomObjectMapper objectMapper = new CustomObjectMapper();
	//// objectMapper.setDefaultAnnotationIntrospector(introspector);
	//// objectMapper.init();
	// return objectMapper;
	// }
	// @Bean
	// @Primary
	//// @ConditionalOnMissingBean
	// ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder,
	// DefaultAnnotationIntrospector introspector) {
	// return
	// builder.createXmlMapper(false).build().setAnnotationIntrospector(introspector);
	// }

	// @Bean
	// public MappingJackson2HttpMessageConverter
	// mappingJackson2HttpMessageConverter(@Autowired ObjectMapper objectMapper) {
	// MappingJackson2HttpMessageConverter converter = new
	// MappingJackson2HttpMessageConverter();
	// converter.setObjectMapper(objectMapper);
	// return converter;
	// }

}
