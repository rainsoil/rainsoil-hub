package com.rainsoil.web.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * /login接口 AfterScript 设置
 *
 *
 * var code=ke.response.data.status;
 * if(code==200){
 *     //判断,如果服务端响应code是8200才执行操作
 *     //获取token
 *     var token=ke.response.data.data;
 *     //1、如何参数是Header，则设置当前逻辑分组下的全局Header
 *     ke.global.setHeader("Authorization",token);
 *     //2、如果全局参数是query类型,则设置当前逻辑分组下的全局Parameter,开发者自行选择
// *     ke.global.setParameter("token",token);
 * }
 * @author luyanan
 * @since 2022/2/27
 **/
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

	@Bean(value = "defaultApi2")
	public Docket defaultApi2() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(new ApiInfoBuilder()
						//.title("swagger-bootstrap-ui-demo RESTful APIs")
						.description("# swagger-bootstrap-ui-demo RESTful APIs")
						.termsOfServiceUrl("http://www.xx.com/")
						.contact("xx@qq.com")
						.version("1.0")
						.build())
//				.securitySchemes(securitySchemes())
//				.securityContexts(securityContexts())
				//分组名称
				.groupName("2.X版本")
				.select()
				//这里指定Controller扫描包路径
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())

				.build();
		return docket;
	}


	private List<ApiKey> securitySchemes() {
		List<ApiKey> apiKeyList = new ArrayList();
		apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
		return apiKeyList;
	}

	private List<SecurityContext> securityContexts() {
		List<SecurityContext> securityContexts = new ArrayList<>();
		securityContexts.add(
				SecurityContext.builder()
						.securityReferences(defaultAuth())
						.forPaths(PathSelectors.regex("^(?!auth).*$"))
						.build());
		return securityContexts;
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		List<SecurityReference> securityReferences = new ArrayList<>();
		securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
		return securityReferences;
	}

}