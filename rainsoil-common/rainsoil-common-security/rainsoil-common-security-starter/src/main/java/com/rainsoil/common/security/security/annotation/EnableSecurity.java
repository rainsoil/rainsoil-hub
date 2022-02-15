package com.rainsoil.common.security.security.annotation;

import com.rainsoil.common.security.security.config.WebSecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启Security安全校验
 *
 * @author luyanan
 * @since 2021/10/5
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@Import(WebSecurityAutoConfiguration.class)
public @interface EnableSecurity {

}
