package com.rainsoil.common.framework.jdbc;

import com.rainsoil.common.framework.jdbc.annotation.FieldStrategy;
import lombok.Data;

/**
 * 全局配置文件
 *
 * @author luyanan
 * @since 2021/3/9
 **/
@Data
public class JdbcGlobalConfig {

	/**
	 * 字段插入的策略,默认插入非空非null的
	 *
	 * @since 2021/3/9
	 */

	private FieldStrategy insertStrategy = FieldStrategy.NOT_EMPTY;

	/**
	 * 字段修改的策略., 默认修改非空非null的
	 *
	 * @author luyanan
	 * @since 2021/3/9
	 */
	private FieldStrategy updateStrategy = FieldStrategy.NOT_EMPTY;

	/**
	 * 字段查询的策略., 默认将实体中非空非null的作为查询条件
	 *
	 * @author luyanan
	 * @since 2021/3/9
	 */
	private FieldStrategy selectStrategy = FieldStrategy.NOT_EMPTY;

	/**
	 * 删除的策略
	 *
	 * @author luyanan
	 * @since 2021/3/14
	 */
	private FieldStrategy deleteStrategy = FieldStrategy.NOT_EMPTY;

}
