package com.rainsoil.common.framework.xss.core;

import java.lang.annotation.*;

/**
 * xss 忽略
 *
 * @author luyanan
 * @since 2021/8/18
 **/
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XssCleanIgnore {

}