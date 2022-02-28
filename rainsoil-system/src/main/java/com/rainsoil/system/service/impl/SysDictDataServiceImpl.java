package com.rainsoil.system.service.impl;

import cn.hutool.core.bean.OptionalBean;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rainsoil.common.data.mybatis.impl.BaseServiceImpl;
import com.rainsoil.core.module.system.vo.SysDictData;
import com.rainsoil.core.utils.DictUtils;
import com.rainsoil.system.mapper.SysDictDataMapper;
import com.rainsoil.system.service.ISysDictDataService;
import com.rainsoil.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysDictDataServiceImpl extends BaseServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {
	@Autowired
	private SysDictDataMapper dictDataMapper;

	@Autowired
	private ISysDictTypeService sysDictTypeService;

	/**
	 * 根据条件分页查询字典数据
	 *
	 * @param dictData 字典数据信息
	 * @return 字典数据集合信息
	 */
	@Override
	public List<SysDictData> selectDictDataList(SysDictData dictData) {
		return dictDataMapper.selectDictDataList(dictData);
	}

	/**
	 * 根据字典类型和字典键值查询字典数据信息
	 *
	 * @param dictType  字典类型
	 * @param dictValue 字典键值
	 * @return 字典标签
	 */
	@Override
	public String selectDictLabel(String dictType, String dictValue) {
		SysDictData sysDictData = super.getOne(new LambdaQueryWrapper<SysDictData>()
				.eq(SysDictData::getDictType, dictType)
				.eq(SysDictData::getDictValue, dictValue));
		return OptionalBean.ofNullable(sysDictData).getBean(SysDictData::getDictLabel).get();
	}

	@Override
	public void updateDictDataType(String oldDictType, String dictType) {
		SysDictData newData = new SysDictData();
		newData.setDictType(dictType);
		super.update(newData, new LambdaUpdateWrapper<SysDictData>().eq(SysDictData::getDictType, dictType));
	}


	@Override
	public boolean removeByIds(Collection<?> idList) {
		for (Object dictCode : idList) {
			SysDictData data = getById((Serializable) dictCode);
			removeById((Serializable) dictCode);
			List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(data.getDictType());
			DictUtils.setDictCache(data.getDictType(), dictDatas);
		}
		return super.removeByIds(idList);
	}

	@Override
	public boolean save(SysDictData entity) {
		boolean save = super.save(entity);
		if (save) {
			List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(entity.getDictType());
			DictUtils.setDictCache(entity.getDictType(), dictDatas);
		}
		return save;
	}


	@Override
	public boolean updateById(SysDictData entity) {
		boolean update = super.updateById(entity);
		if (update) {
			List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(entity.getDictType());
			DictUtils.setDictCache(entity.getDictType(), dictDatas);
		}
		return update;
	}
}
