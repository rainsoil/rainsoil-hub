package com.rainsoil.system.controller;

import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.core.module.system.vo.SysDictData;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.service.ISysDictDataService;
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
 * 数据字典信息
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Api(value = "数据字典")
@Controller
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
	private String prefix = "system/dict/data";

	private final ISysDictDataService dictDataService;


	/**
	 * 分页列表
	 *
	 * @param requestParams
	 * @return com.rainsoil.core.core.page.TableDataInfo
	 * @since 2021/9/24
	 */
	@ApiOperation(value = "分页列表")
	@PostMapping("/list")
	@PreAuthorize("@ss.hasPermi('system:dict:list')")
	@ResponseBody
	public RespEntity<PageInfo<SysDictData>> list(@RequestBody PageRequestParams<SysDictData> requestParams) {
		PageInfo<SysDictData> pageInfo = dictDataService.page(requestParams);
		return RespEntity.success(pageInfo);
	}

//	@PreAuthorize("@ss.hasPermi('system:dict:export')")
//	@PostMapping("/export")
//	@ResponseBody
//	public RespEntity export(SysDictData dictData) {
//		List<SysDictData> list = dictDataService.selectDictDataList(dictData);
//		ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
//		return util.exportExcel(list, "字典数据");
//	}


	/**
	 * 新增保存字典类型
	 *
	 * @param dict
	 * @return com.rainsoil.core.core.domain.AjaxResult
	 * @since 2021/9/24
	 */
	@ApiOperation(value = "新增保存字典类型")
	@PreAuthorize("@ss.hasPermi('system:dict:add')")
	@PostMapping("/add")
	@ResponseBody
	public RespEntity addSave(@Validated SysDictData dict) {
		dictDataService.save(dict);
		return RespEntity.success();
	}


	/**
	 * 修改保存字典类型
	 *
	 * @param dict
	 * @return RespEntity
	 * @since 2021/9/24
	 */
	@ApiOperation(value = "修改保存字典类型")
	@PreAuthorize("@ss.hasPermi('system:dict:edit')")
	@PostMapping("/edit")
	@ResponseBody
	public RespEntity editSave(@Validated SysDictData dict) {

		dictDataService.updateById(dict);
		return RespEntity.success();
	}

	/**
	 * 删除
	 *
	 * @param ids
	 * @return com.rainsoil.core.core.domain.AjaxResult
	 * @since 2021/9/24
	 */
	@ApiOperation(value = "删除")
	@PreAuthorize("@ss.hasPermi('system:dict:remove')")
	@PostMapping("/remove")
	@ResponseBody
	public RespEntity remove(String ids) {
		dictDataService.removeByIds(Arrays.asList(ids.split(",")));
		return RespEntity.success();
	}

	/**
	 * 跳转列表
	 *
	 * @return java.lang.String
	 * @since 2021/9/24
	 */
	@ApiOperation(value = "跳转列表")
	@PreAuthorize("@ss.hasPermi('system:dict:view')")
	@GetMapping()
	public String dictData() {
		return prefix + "/data";
	}

	/**
	 * 新增字典类型
	 *
	 * @param dictType
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/24
	 */
	@ApiOperation(value = "新增字典类型")
	@GetMapping("/add/{dictType}")
	public String add(@PathVariable("dictType") String dictType, ModelMap mmap) {
		mmap.put("dictType", dictType);
		return prefix + "/add";
	}

	/**
	 * 修改字典类型
	 *
	 * @param dictCode
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/24
	 */
	@ApiOperation(value = " 修改字典类型")
	@GetMapping("/edit/{dictCode}")
	public String edit(@PathVariable("dictCode") Long dictCode, ModelMap mmap) {
		mmap.put("dict", dictDataService.getById(dictCode));
		return prefix + "/edit";
	}

}
