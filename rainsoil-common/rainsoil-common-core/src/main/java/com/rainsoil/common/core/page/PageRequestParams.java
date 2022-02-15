package com.rainsoil.common.core.page;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页请求参数封装
 *
 * @param <T> 泛型
 * @author luyanan
 * @since 2021/8/13
 **/
public class PageRequestParams<T> implements Serializable {

	/**
	 * 首页
	 *
	 * @since 2022/2/7
	 */

	public static final long FIRST_PAGE_INDEX = 1L;

	/**
	 * 当前页数
	 *
	 * @since 2021/8/13
	 */
	@Setter
	private Long pageNum = FIRST_PAGE_INDEX;

	/**
	 * 每页条数
	 *
	 * @since 2021/8/13
	 */
	@Getter
	@Setter
	private Long pageSize = 10L;

	/**
	 * 请求参数
	 *
	 * @since 2021/8/13
	 */
	@Getter
	@Setter
	private T params;

	/**
	 * 游标
	 *
	 * @since 2021/8/13
	 */

	@Setter
	private Long offset;

	/**
	 * 排序
	 *
	 * @since 2021/8/18
	 */

	@Getter
	@Setter
	private List<OrderItem> orders;

	/**
	 * 构建
	 *
	 * @param params 参数
	 * @param offset 下标
	 * @param limit  limit
	 * @param <T>    泛型
	 * @return com.rainsoil.common.core.page.PageRequestParams<T>
	 * @since 2022/2/7
	 */
	public static <T> PageRequestParams<T> build(T params, Long offset, Long limit) {

		PageRequestParams<T> pageRequestParams = new PageRequestParams<>();
		pageRequestParams.setOffset(offset);
		pageRequestParams.setPageSize(limit);
		pageRequestParams.setParams(params);
		return pageRequestParams;
	}

	/**
	 * 获取offset
	 *
	 * @return java.lang.Integer
	 * @since 2022/2/8
	 */
	public Long getOffset() {
		if (this.offset == null) {
			if (this.pageNum <= 0) {
				return this.pageNum * this.pageSize;
			} else {
				return (this.pageNum - 1) * this.pageSize;
			}
		} else {
			return this.offset;
		}
	}

	/**
	 * 当前页数
	 *
	 * @return java.lang.Integer
	 * @since 2022/2/8
	 */
	public Long getPageNum() {
		if (offset != null && pageNum == null) {
			return offset / pageSize + FIRST_PAGE_INDEX;
		}
		return pageNum;
	}

}
