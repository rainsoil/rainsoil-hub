package com.rainsoil.common.security.oauth.client.annotation;


import com.rainsoil.common.security.oauth.client.config.OauthClientAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Oauth2.0 的客户端注解
 *
 * @author luyanan
 * @since 2021/10/25
 **/
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(OauthClientAutoConfiguration.class)
public @interface EnableOauthClient {

}
