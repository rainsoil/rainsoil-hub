package com.rainsoil.system.controller;

import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.domain.SysPost;
import com.rainsoil.system.service.ISysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 岗位信息操作处理
 *
 * @author ruoyi
 */
@Api(value = "岗位信息")
@Controller
@RequestMapping("/system/post")
public class SysPostController extends BaseController {
	private String prefix = "system/post";

	@Autowired
	private ISysPostService postService;



	/**
	 * 分页列表
	 *
	 * @param requestParams
	 * @return RespEntity<PageInfo>
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "分页列表")
	@PreAuthorize("@ss.hasPermi('system:post:list')")
	@PostMapping("/list")
	@ResponseBody
	public RespEntity<PageInfo> list(@RequestBody PageRequestParams<SysPost> requestParams) {
		PageInfo<SysPost> pageInfo = postService.page(requestParams);
		return RespEntity.success(pageInfo);
	}
//
//	@PreAuthorize("@ss.hasPermi('system:post:export')")
//	@PostMapping("/export")
//	@ResponseBody
//	public RespEntity export(SysPost post) {
//		List<SysPost> list = postService.list(new LambdaQueryWrapper<>(post));
//		ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
//		return util.exportExcel(list, "岗位数据");
//	}

	/**
	 * 删除
	 *
	 * @param ids
	 * @return RespEntity
	 * @since 2021/9/23
	 */
	@PreAuthorize("@ss.hasPermi('system:post:remove')")
	@PostMapping("/remove")
	@ResponseBody
	public RespEntity remove(String ids) {

		postService.removeByIds(Arrays.asList(ids.split(",")));
		return RespEntity.success();
	}



	/**
	 * 新增保存岗位
	 *
	 * @param post
	 * @return RespEntity
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "新增保存岗位")
	@PreAuthorize("@ss.hasPermi('system:post:add')")
	@PostMapping("/add")
	@ResponseBody
	public RespEntity addSave(@Validated SysPost post) {
		return RespEntity.success(postService.save(post));
	}



	/**
	 * 修改保存岗位
	 *
	 * @param post
	 * @return RespEntity
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "修改保存岗位")
	@PreAuthorize("@ss.hasPermi('system:post:edit')")
	@PostMapping("/edit")
	@ResponseBody
	public RespEntity editSave(@Validated SysPost post) {

		return RespEntity.success(postService.updateById(post));
	}

	/**
	 * 校验岗位名称
	 *
	 * @param post
	 * @return RespEntity
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "校验岗位名称")
	@PostMapping("/checkPostNameUnique")
	@ResponseBody
	public RespEntity checkPostNameUnique(SysPost post) {
		postService.checkPostNameUnique(post);
		return RespEntity.success();
	}


	/**
	 * 校验岗位编码
	 *
	 * @param post
	 * @return RespEntity
	 * @since 2021/9/23
	 */
	@ApiOperation(value = "校验岗位编码")
	@PostMapping("/checkPostCodeUnique")
	@ResponseBody
	public RespEntity checkPostCodeUnique(SysPost post) {
		postService.checkPostCodeUnique(post);
		return RespEntity.success();
	}

	@PreAuthorize("@ss.hasPermi('system:post:view')")
	@GetMapping()
	public String operlog() {
		return prefix + "/post";
	}

	/**
	 * 新增岗位
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}


	/**
	 * 修改岗位
	 *
	 * @param postId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/23
	 */
	@ApiOperation(value = " 修改岗位")
	@GetMapping("/edit/{postId}")
	public String edit(@PathVariable("postId") Long postId, ModelMap mmap) {
		mmap.put("post", postService.getById(postId));
		return prefix + "/edit";
	}
}
