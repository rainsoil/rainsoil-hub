package com.rainsoil.common.core.page;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 排序实体
 *
 * @author luyanan
 * @since 2021/8/18
 **/
@Data
public class OrderItem implements Serializable {

	private static final long serialVersionUID = 3613312305980905219L;

	/**
	 * 字段
	 *
	 * @since 2022/2/7
	 */

	private String column;

	/**
	 * 升序
	 *
	 * @since 2022/2/7
	 */

	private boolean asc = true;

	/**
	 * 升序的字段
	 *
	 * @param column 字段
	 * @return com.rainsoil.common.core.page.OrderItem
	 * @since 2022/2/7
	 */
	public static OrderItem asc(String column) {
		return build(column, true);
	}

	/**
	 * 降序的字段
	 *
	 * @param column 字段
	 * @return com.rainsoil.common.core.page.OrderItem
	 * @since 2022/2/7
	 */
	public static OrderItem desc(String column) {
		return build(column, false);
	}

	/**
	 * 设置升序的字段
	 *
	 * @param columns 字段
	 * @return java.util.List<com.rainsoil.common.core.page.OrderItem>
	 * @since 2022/2/7
	 */
	public static List<OrderItem> ascs(String... columns) {
		return (List) Arrays.stream(columns).map(OrderItem::asc).collect(Collectors.toList());
	}

	/**
	 * 设置降序的字段
	 *
	 * @param columns 字段
	 * @return java.util.List<com.rainsoil.common.core.page.OrderItem>
	 * @since 2022/2/7
	 */
	public static List<OrderItem> descs(String... columns) {
		return (List) Arrays.stream(columns).map(OrderItem::desc).collect(Collectors.toList());
	}


	/**
	 * 构建
	 *
	 * @param column 字段
	 * @param asc    是否升序
	 * @return com.rainsoil.common.core.page.OrderItem
	 * @since 2022/2/7
	 */
	public static OrderItem build(String column, boolean asc) {
		OrderItem item = new OrderItem();
		item.setColumn(column);
		item.setAsc(asc);
		return item;
	}

}
