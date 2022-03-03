package com.rainsoil.common.framework.jdbc;

import java.text.MessageFormat;

/**
 * SQL关键字
 *
 * @author luyanan
 * @since 2021/3/8
 **/
public enum SqlKeyword {

	/**
	 * AND
	 *
	 * @since 2021/8/18
	 */

	AND(" AND {0} = ?"),
	/**
	 * OR
	 *
	 * @since 2021/8/18
	 */

	OR("OR"),
	/**
	 * IN
	 *
	 * @since 2021/8/18
	 */

	IN(" AND {0} IN "),
	/**
	 * NOT
	 *
	 * @since 2021/8/18
	 */

	NOT("NOT"),
	/**
	 * LIKE
	 *
	 * @since 2021/8/18
	 */

	LIKE("LIKE CONCAT ({0},?,{1}) "),
	/**
	 * EQ
	 *
	 * @since 2021/8/18
	 */

	EQ("{0} = ?"),
	/**
	 * NE
	 *
	 * @since 2021/8/18
	 */

	NE("{0} <> ?"),
	/**
	 * GT
	 *
	 * @since 2021/8/18
	 */

	GT("{0} > ?"),
	/**
	 * GE
	 *
	 * @since 2021/8/18
	 */

	GE("{0} >= ?"),
	/**
	 * LT
	 *
	 * @since 2021/8/18
	 */

	LT("{0} < ?"),
	/**
	 * LE
	 *
	 * @since 2021/8/18
	 */

	LE("{0} <= ?"),
	/**
	 * IS_NULL
	 *
	 * @since 2021/8/18
	 */

	IS_NULL("{0} IS NULL"),
	/**
	 * IS_NOT_NULL
	 *
	 * @since 2021/8/18
	 */

	IS_NOT_NULL("{0} IS NOT NULL"),
	/**
	 * GROUP_BY
	 *
	 * @since 2021/8/18
	 */

	GROUP_BY("GROUP BY {0}"),
	/**
	 * HAVING
	 *
	 * @since 2021/8/18
	 */

	HAVING("HAVING"),
	/**
	 * ORDER_BY
	 *
	 * @since 2021/8/18
	 */

	ORDER_BY("ORDER BY {0}"),
	/**
	 * EXISTS
	 *
	 * @since 2021/8/18
	 */

	EXISTS("EXISTS"),
	/**
	 * BETWEEN
	 *
	 * @since 2021/8/18
	 */

	BETWEEN("BETWEEN ? AND ? "),
	/**
	 * ASC
	 *
	 * @since 2021/8/18
	 */

	ASC("ASC"),
	/**
	 * DESC
	 *
	 * @since 2021/8/18
	 */

	DESC("DESC");

	private final String keyword;

	SqlKeyword(final String keyword) {
		this.keyword = keyword;
	}

	/**
	 * 获取SQL填充
	 *
	 * @param args 参数
	 * @return java.lang.String
	 * @since 2022/3/3
	 */
	public String getSqlSegment(Object... args) {
		return MessageFormat.format(this.keyword, args);
	}

}
