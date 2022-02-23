package com.rainsoil.system.service;



import com.rainsoil.common.data.mybatis.IBaseService;
import com.rainsoil.core.module.system.vo.SysDictData;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author ruoyi
 */
public interface ISysDictDataService extends IBaseService<SysDictData> {
	/**
	 * 根据条件分页查询字典数据
	 *
	 * @param dictData 字典数据信息
	 * @return 字典数据集合信息
	 */
	List<SysDictData> selectDictDataList(SysDictData dictData);

	/**
	 * 根据字典类型和字典键值查询字典数据信息
	 *
	 * @param dictType  字典类型
	 * @param dictValue 字典键值
	 * @return 字典标签
	 */
	String selectDictLabel(String dictType, String dictValue);

	/**
	 * 修改dictType
	 *
	 * @param oldDictType 老的dictType
	 * @param dictType    新的dictType
	 * @return void
	 * @since 2021/9/25
	 */
	void updateDictDataType(String oldDictType, String dictType);

//	/**
//	 * 根据字典数据ID查询信息
//	 *
//	 * @param dictCode 字典数据ID
//	 * @return 字典数据
//	 */
//	public SysDictData selectDictDataById(Long dictCode);
//
//	/**
//	 * 批量删除字典数据
//	 *
//	 * @param ids 需要删除的数据
//	 * @return 结果
//	 */
//	public void deleteDictDataByIds(String ids);


}
