package com.rainsoil.system.controller;

import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.domain.SysNotice;
import com.rainsoil.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@Api(value = "公告")
@RequiredArgsConstructor
@Controller
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {
	private String prefix = "system/notice";
	private final ISysNoticeService noticeService;



	/**
	 * 查询公告列表
	 *
	 * @param pageRequestParams
	 * @return RespEntity<PageInfo>
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "查询公告列表")
	@PreAuthorize("@ps.hasPermi('system:notice:list')")
	@PostMapping("/list")
	@ResponseBody
	public RespEntity<PageInfo> list(@RequestBody PageRequestParams<SysNotice> pageRequestParams) {
		PageInfo<SysNotice> pageInfo = noticeService.page(pageRequestParams);
		return RespEntity.success(pageInfo);
	}



	/**
	 * 新增保存公告
	 *
	 * @param notice
	 * @return RespEntity
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "新增保存公告")
	@PreAuthorize("@ps.hasPermi('system:notice:add')")
	@PostMapping("/add")
	@ResponseBody
	public RespEntity addSave(@Validated SysNotice notice) {
		noticeService.save(notice);
		return RespEntity.success();
	}



	/**
	 * 修改保存公告
	 *
	 * @param notice
	 * @return RespEntity
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "修改保存公告")
	@PreAuthorize("@ps.hasPermi('system:notice:edit')")
	@PostMapping("/edit")
	@ResponseBody
	public RespEntity editSave(@Validated SysNotice notice) {
		return RespEntity.success(noticeService.updateById(notice));
	}

	/**
	 * 删除公告
	 *
	 * @param ids
	 * @return RespEntity
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "删除公告")
	@PreAuthorize("@ps.hasPermi('system:notice:remove')")
	@PostMapping("/remove")
	@ResponseBody
	public RespEntity remove(String ids) {
		noticeService.removeByIds(Arrays.asList(ids.split(",")));
		return RespEntity.success();
	}



	/**
	 * 跳转列表页面
	 *
	 * @return java.lang.String
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "跳转列表页面")
	@PreAuthorize("@ps.hasPermi('system:notice:view')")
	@GetMapping()
	public String notice() {
		return prefix + "/notice";
	}


	/**
	 * 新增公告
	 *
	 * @return java.lang.String
	 * @since 2021/9/23
	 */
	@ApiOperation(value = " 新增公告")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}


	/**
	 * 修改公告
	 *
	 * @param noticeId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "修改公告")
	@GetMapping("/edit/{noticeId}")
	public String edit(@PathVariable("noticeId") Long noticeId, ModelMap mmap) {
		mmap.put("notice", noticeService.getById(noticeId));
		return prefix + "/edit";
	}
}
