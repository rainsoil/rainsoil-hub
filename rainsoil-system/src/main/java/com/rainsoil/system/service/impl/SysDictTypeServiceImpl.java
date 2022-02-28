package com.rainsoil.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rainsoil.common.core.page.PageInfo;
import com.rainsoil.common.core.page.PageRequestParams;
import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.core.constant.UserConstants;
import com.rainsoil.core.module.system.vo.SysDictData;
import com.rainsoil.core.module.system.vo.SysDictType;
import com.rainsoil.core.module.system.vo.Ztree;
import com.rainsoil.core.utils.DictUtils;
import com.rainsoil.core.utils.StringUtil;
import com.rainsoil.system.SystemCode;
import com.rainsoil.system.exception.SystemException;
import com.rainsoil.system.mapper.SysDictTypeMapper;
import com.rainsoil.system.service.ISysDictDataService;
import com.rainsoil.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysDictTypeServiceImpl extends BaseServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {


	@Autowired
	private ISysDictDataService sysDictDataService;

	/**
	 * 项目启动时，初始化字典到缓存
	 */
	@PostConstruct
	public void init() {
		loadingDictCache();
	}


	@Override
	public PageInfo<SysDictType> page(PageRequestParams<SysDictType> pageRequestParams) {
		LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper();
		SysDictType params = pageRequestParams.getParams();
		queryWrapper.setEntity(params);
		queryWrapper.apply(!StrUtil.isBlankIfStr(params.getBeginDate()),
				"date_format(create_time,'%y%m%d') >= date_format({0},'%y%m%d')", params.getBeginDate());
		queryWrapper.apply(!StrUtil.isBlankIfStr(params.getEndDate()),
				"date_format(create_time,'%y%m%d') <= date_format({0},'%y%m%d')", params.getEndDate());
		return super.page(pageRequestParams, queryWrapper);
	}

	/**
	 * 根据字典类型查询字典数据
	 *
	 * @param dictType 字典类型
	 * @return 字典数据集合信息
	 */
	@Override
	public List<SysDictData> selectDictDataByType(String dictType) {
		List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
		if (StringUtil.isNotEmpty(dictDatas)) {
			return dictDatas;
		}
		SysDictData param = new SysDictData();
		param.setStatus("0");
		param.setDictType(dictType);
		dictDatas = sysDictDataService.list(param);
		if (StringUtil.isNotEmpty(dictDatas)) {
			DictUtils.setDictCache(dictType, dictDatas);
			return dictDatas;
		}
		return null;
	}


	@Override
	public boolean removeByIds(Collection<?> idList) {
		for (Object dictId : idList) {
			SysDictType dictType = getById((Serializable) dictId);
			if (sysDictDataService.count(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getDictType, dictType.getDictType())) > 0) {
				throw new SystemException(SystemCode.POST_USER_EXIST, dictType.getDictName());
			}
			super.removeById((Serializable) dictId);
			DictUtils.removeDictCache(dictType.getDictType());
		}
		return true;
	}

	/**
	 * 加载字典缓存数据
	 */
	@Override
	public void loadingDictCache() {
		List<SysDictType> dictTypeList = list();
		for (SysDictType dict : dictTypeList) {
			List<SysDictData> dictDatas = sysDictDataService.list(new LambdaQueryWrapper<SysDictData>()
					.eq(SysDictData::getDictType, dict.getDictType()));
			DictUtils.setDictCache(dict.getDictType(), dictDatas);
		}
	}

	/**
	 * 清空字典缓存数据
	 */
	@Override
	public void clearDictCache() {
		DictUtils.clearDictCache();
	}

	/**
	 * 重置字典缓存数据
	 */
	@Override
	public void resetDictCache() {
		clearDictCache();
		loadingDictCache();
	}


	@Override
	public boolean save(SysDictType entity) {
		checkDictTypeUnique(entity);
		boolean save = super.save(entity);
		if (save) {
			DictUtils.setDictCache(entity.getDictType(), null);
		}
		return save;
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateById(SysDictType entity) {
		checkDictTypeUnique(entity);
		SysDictType oldDict = getById(entity.getDictId());
		if (null == oldDict) {
			return false;
		}
		sysDictDataService.updateDictDataType(oldDict.getDictType(), entity.getDictType());
		boolean update = super.updateById(entity);

		if (update) {
			List<SysDictData> dictDatas = selectDictDataByType(entity.getDictType());
			DictUtils.setDictCache(entity.getDictType(), dictDatas);
		}
		return update;
	}

	/**
	 * 校验字典类型称是否唯一
	 *
	 * @param dict 字典类型
	 * @return 结果
	 */
	@Override
	public void checkDictTypeUnique(SysDictType dict) {
		Long dictId = StringUtil.isNull(dict.getDictId()) ? -1L : dict.getDictId();
		SysDictType dictType = getOne(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictType, dict.getDictType()));
		if (StringUtil.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue()) {
			throw new SystemException(SystemCode.DATA_EXIST, dict.getDictType());
		}
	}

	/**
	 * 查询字典类型树
	 *
	 * @param dictType 字典类型
	 * @return 所有字典类型
	 */
	@Override
	public List<Ztree> selectDictTree(SysDictType dictType) {
		List<Ztree> ztrees = new ArrayList<Ztree>();
		List<SysDictType> dictList = super.list(dictType);
		for (SysDictType dict : dictList) {
			if (UserConstants.DICT_NORMAL.equals(dict.getStatus())) {
				Ztree ztree = new Ztree();
				ztree.setId(dict.getDictId());
				ztree.setName(transDictName(dict));
				ztree.setTitle(dict.getDictType());
				ztrees.add(ztree);
			}
		}
		return ztrees;
	}

	public String transDictName(SysDictType dictType) {
		StringBuffer sb = new StringBuffer();
		sb.append("(" + dictType.getDictName() + ")");
		sb.append("&nbsp;&nbsp;&nbsp;" + dictType.getDictType());
		return sb.toString();
	}
}
