package com.rainsoil.system.controller;

import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.core.page.RespEntity;
import com.rainsoil.core.service.BaseController;
import com.rainsoil.system.domain.SysConfig;
import com.rainsoil.system.service.ISysConfigService;
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
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@Api(value = "系统配置")
@Controller
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
	private String prefix = "system/config";

	@Autowired
	private ISysConfigService configService;


	/**
	 * 配置列表
	 *
	 * @param requestParams
	 * @return com.rainsoil.core.core.page.TableDataInfo
	 * @since 2021/9/17
	 */
	@ApiOperation(value = "配置列表")
	@PreAuthorize("@ss.hasPermi('system:config:list')")
	@PostMapping("/list")
	@ResponseBody
	public RespEntity<PageInfo> list(@RequestBody PageRequestParams<SysConfig> requestParams) {
//		startPage();
//		List<SysConfig> list = configService.selectConfigList(config);
//		return getDataTable(list);

		PageInfo<SysConfig> page = configService.page(requestParams);
		return RespEntity.success(page);
	}


//	/**
//	 * 配置导出
//	 *
//	 * @param config
//	 * @return com.rainsoil.core.core.domain.AjaxResult
//	 * @since 2021/9/17
//	 */
//	@IgnoreLogger(type = IgnoreLogger.Type.RESULT)
//	@ApiOperation(value = "配置导出")
//	@PreAuthorize("@ss.hasPermi('system:config:export')")
//	@PostMapping("/export")
//	@ResponseBody
//	public RespEntity export(SysConfig config) {
//		List<SysConfig> list = configService.selectConfigList(config);
//		ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
//		return util.exportExcel(list, "参数数据");
//	}


	/**
	 * 参数添加
	 *
	 * @param config
	 * @return com.rainsoil.core.core.domain.AjaxResult
	 * @since 2021/9/17
	 */
	@ApiOperation(value = "参数添加")
	@PreAuthorize("@ss.hasPermi('system:config:add')")
	@PostMapping("/add")
	@ResponseBody
	public RespEntity addSave(@Validated SysConfig config) {
		return RespEntity.success(configService.save(config));
	}


	/**
	 * 配置修改
	 *
	 * @param config
	 * @return com.rainsoil.core.core.domain.AjaxResult
	 * @since 2021/9/17
	 */
	@ApiOperation(value = "配置修改")
	@PreAuthorize("@ss.hasPermi('system:config:edit')")
	@PostMapping("/edit")
	@ResponseBody
	public RespEntity editSave(@Validated SysConfig config) {
		return RespEntity.success(configService.updateById(config));
	}


	/**
	 * 根据ids删除配置
	 *
	 * @param ids
	 * @return com.rainsoil.core.core.domain.AjaxResult
	 * @since 2021/9/18
	 */
	@ApiOperation(value = "刷新参数缓存")
	@PreAuthorize("@ss.hasPermi('system:config:remove')")
	@PostMapping("/remove")
	@ResponseBody
	public RespEntity remove(String ids) {
		configService.removeByIds(Arrays.asList(ids.split(",")));
		return RespEntity.success();
	}


	/**
	 * 刷新参数缓存
	 *
	 * @return com.rainsoil.core.core.domain.AjaxResult
	 * @since 2021/9/18
	 */
	@ApiOperation(value = "根据ids删除配置")
	@PreAuthorize("@ss.hasPermi('system:config:remove')")
	@GetMapping("/refreshCache")
	@ResponseBody
	public RespEntity refreshCache() {
		configService.resetConfigCache();
		return RespEntity.success();
	}


	/**
	 * 校验参数键名
	 *
	 * @param config
	 * @return java.lang.String
	 * @since 2021/9/18
	 */
	@ApiOperation(value = "校验参数键名")
	@PostMapping("/checkConfigKeyUnique")
	@ResponseBody
	public RespEntity checkConfigKeyUnique(SysConfig config) {
		configService.checkConfigKeyUnique(config);
		return RespEntity.success();
	}


	/**
	 * 跳转系统配置
	 *
	 * @return java.lang.String
	 * @since 2021/9/17
	 */
	@ApiOperation(value = "跳转系统配置")
	@PreAuthorize("@ss.hasPermi('system:config:view')")
	@GetMapping()
	public String config() {
		return prefix + "/config";
	}


	/**
	 * 跳转到添加页面
	 *
	 * @return java.lang.String
	 * @since 2021/9/17
	 */
	@ApiOperation(value = "跳转到添加页面")
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}


	/**
	 * 跳转编辑页面
	 *
	 * @param configId
	 * @param mmap
	 * @return java.lang.String
	 * @since 2021/9/17
	 */
	@ApiOperation(value = "跳转编辑页面")
	@GetMapping("/edit/{configId}")
	public String edit(@PathVariable("configId") Long configId, ModelMap mmap) {
		mmap.put("config", configService.getById(configId));
		return prefix + "/edit";
	}
}
