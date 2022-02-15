package com.rainsoil.common.security.oauth.server.annotation;

import com.rainsoil.common.security.oauth.server.config.OauthServerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Oauth2.0 服务端的配置
 *
 * @author luyanan
 * @since 2021/10/26
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@Import(OauthServerAutoConfiguration.class)
public @interface EnableOauthServer {

}
