package com.rainsoil.common.framework.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * spring cache自动配置类
 *
 * @author luyanan
 * @since 2021/9/20
 **/
@Configuration
@ConditionalOnBean(annotation = EnableCaching.class)
@Import(CacheConfig.class)
public class SpringCacheAutoConfiguraion {

}