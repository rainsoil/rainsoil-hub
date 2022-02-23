package com.rainsoil.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.core.module.system.vo.SysDictType;
import com.rainsoil.core.module.system.vo.Ztree;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@Api(value = "数据字典信息")
@Controller
@RequestMapping("/system/dict")
public class SysDictTypeController extends BaseController {

	private String prefix = "system/dict/type";
	@Autowired
	private ISysDictTypeService dictTypeService;


	/**
	 * 列表
	 *
	 * @param requestParams
	 * @return RespEntity<PageInfo < com.rainsoil.core.module.system.vo.SysDictType>>
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "列表")
	@PostMapping("/list")
	@PreAuthorize("@ss.hasPermi('system:dict:list')")
	@ResponseBody
	public RespEntity<PageInfo<SysDictType>> list(@RequestBody PageRequestParams<SysDictType> requestParams) {
		PageInfo<SysDictType> pageInfo = dictTypeService.page(requestParams);
		return RespEntity.success(pageInfo);
	}

//	@PreAuthorize("@ss.hasPermi('system:dict:export')")
//	@PostMapping("/export")
//	@ResponseBody
//	public RespEntity export(SysDictType dictType) {
//
//		List<SysDictType> list = dictTypeService.list(new LambdaQueryWrapper<>(dictType));
//		ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
//		return util.exportExcel(list, "字典类型");
//	}


	/**
	 * 新增保存字典类型
	 *
	 * @param dict
	 * @return com.rainsoil.core.core.domain.AjaxResult
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "新增保存字典类型")
	@PreAuthorize("@ss.hasPermi('system:dict:add')")
	@PostMapping("/add")
	@ResponseBody
	public RespEntity addSave(@Validated SysDictType dict) {
		dictTypeService.save(dict);
		return RespEntity.success();
	}


	/**
	 * 修改保存字典类型
	 *
	 * @param dict
	 * @return RespEntity
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "修改保存字典类型")
	@PreAuthorize("@ss.hasPermi('system:dict:edit')")
	@PostMapping("/edit")
	@ResponseBody
	public RespEntity editSave(@Validated SysDictType dict) {
		dictTypeService.updateById(dict);
		return RespEntity.success();
	}

	/**
	 * 删除
	 *
	 * @param ids
	 * @return RespEntity
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "删除")
	@PreAuthorize("@ss.hasPermi('system:dict:remove')")
	@PostMapping("/remove")
	@ResponseBody
	public RespEntity remove(String ids) {
		dictTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return RespEntity.success();
	}


	/**
	 * 刷新字典缓存
	 *
	 * @return RespEntity
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "刷新字典缓存")
	@PreAuthorize("@ss.hasPermi('system:dict:remove')")
	@GetMapping("/refreshCache")
	@ResponseBody
	public RespEntity refreshCache() {
		dictTypeService.resetDictCache();
		return RespEntity.success();
	}


	/**
	 * 校验字典类型
	 */
	@PostMapping("/checkDictTypeUnique")
	@ResponseBody
	public RespEntity checkDictTypeUnique(SysDictType dictType) {
		dictTypeService.checkDictTypeUnique(dictType);
		return RespEntity.success();
	}


	/**
	 * 加载字典列表树
	 */
	@GetMapping("/treeData")
	@ResponseBody
	public List<Ztree> treeData() {
		List<Ztree> ztrees = dictTypeService.selectDictTree(new SysDictType());
		return ztrees;
	}

	/**
	 * 跳转列表页面
	 *
	 * @return java.lang.String
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "跳转列表页面")
	@PreAuthorize("@ss.hasPermi('system:dict:view')")
	@GetMapping()
	public String dictType() {
		return prefix + "/type";
	}


	/**
	 * 新增字典类型
	 *
	 * @return java.lang.String
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "新增字典类型")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 修改字典类型
	 *
	 * @param dictId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "修改字典类型")
	@GetMapping("/edit/{dictId}")
	public String edit(@PathVariable("dictId") Long dictId, ModelMap mmap) {
		mmap.put("dict", dictTypeService.getById(dictId));
		return prefix + "/edit";
	}

	/**
	 * 查询字典详细
	 *
	 * @param dictId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/25
	 */
	@ApiOperation(value = "查询字典详细")
	@PreAuthorize("@ss.hasPermi('system:dict:list')")
	@GetMapping("/detail/{dictId}")
	public String detail(@PathVariable("dictId") Long dictId, ModelMap mmap) {
		mmap.put("dict", dictTypeService.getById(dictId));
		mmap.put("dictList", dictTypeService.list());
		return "system/dict/data/data";
	}

	/**
	 * 选择字典树
	 */
	@GetMapping("/selectDictTree/{columnId}/{dictType}")
	public String selectDeptTree(@PathVariable("columnId") Long columnId, @PathVariable("dictType") String dictType,
								 ModelMap mmap) {
		mmap.put("columnId", columnId);
		mmap.put("dict", dictTypeService.getOne(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictType, dictType)));
		return prefix + "/tree";
	}

}
