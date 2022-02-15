package com.rainsoil.common.core.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 分页
 *
 * @param <T> 泛型
 * @author luyanan
 * @since 2022/2/7
 **/
@NoArgsConstructor
@ApiModel("分页信息")
public class PageInfo<T> {

	/**
	 * 当前页数
	 *
	 * @since 2021/8/13
	 */
	@Getter
	@Setter
	@ApiModelProperty(value = "当前页数")
	private Long pageNum;

	/**
	 * 每页条数
	 *
	 * @since 2021/8/13
	 */
	@Getter
	@Setter
	@ApiModelProperty(value = " 每页条数")
	private Long pageSize;

	/**
	 * 总条数
	 *
	 * @since 2021/8/13
	 */
	@Getter
	@Setter
	@ApiModelProperty(value = "总条数")
	private Long totalElements;

	/**
	 * 内容
	 *
	 * @since 2021/8/13
	 */
	@Getter
	@Setter
	@ApiModelProperty(value = "内容")
	private List<T> content;

	/**
	 * 总页数
	 *
	 * @since 2021/8/13
	 */
	@ApiModelProperty(value = "总页数")
	private Long totalPages;

	/**
	 * 是否为起始页
	 *
	 * @since 2021/8/13
	 */
	@Getter
	@ApiModelProperty(value = "是否为起始页")
	private Boolean first;

	/**
	 * 是否为结尾页
	 *
	 * @since 2021/8/13
	 */
	@Getter
	@Setter
	@ApiModelProperty(value = "是否为结尾页")
	private Boolean last;

	/**
	 * 获取总页数
	 *
	 * @return java.lang.Long
	 * @since 2022/2/7
	 */
	public Long getTotalPages() {
		if (null != this.totalElements) {
			return this.totalElements / pageSize + 1;
		}
		return 0L;
	}

	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * 是否为首页
	 *
	 * @return boolean
	 * @since 2022/2/7
	 */
	public boolean getFirst() {
		return this.getPageNum() == PageRequestParams.FIRST_PAGE_INDEX;
	}


	public PageInfo(List content, Long totalElements, PageRequestParams pageRequestParams) {
		this.totalElements = totalElements;
		this.content = content;
		this.pageNum = pageRequestParams.getPageNum();
		this.pageSize = pageRequestParams.getPageSize();
	}

}