package com.rainsoil.common.core.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 字段缓存
 *
 * @author luyanan
 * @since 2021/3/8
 **/
@Data
@AllArgsConstructor
public class ColumnCache implements Serializable {

	private static final long serialVersionUID = -6248363753155972896L;

	/**
	 * 使用column
	 *
	 * @since 2021/3/8
	 */

	private String column;

	/**
	 * 查询 columnSelect
	 *
	 * @since 2021/3/8
	 */

	private String columnSelect;

}
