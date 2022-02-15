package com.rainsoil.common.file.core.annotation;

import java.lang.annotation.*;

/**
 * 类或者方法忽略文件host处理注解
 *
 * @author luyanan
 * @since 2021/10/14
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Inherited
public @interface FileHostIgnore {

	/**
	 * 忽略添加host
	 * @return boolean
	 * @since 2021/10/14
	 */
	boolean addHost() default true;

	/**
	 * 忽略移除host
	 * @return boolean
	 * @since 2021/10/14
	 */
	boolean removeHost() default true;

}
