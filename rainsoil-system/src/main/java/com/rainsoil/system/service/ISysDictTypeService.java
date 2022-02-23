package com.rainsoil.system.service;



import com.rainsoil.common.data.mybatis.IBaseService;
import com.rainsoil.core.module.system.vo.SysDictData;
import com.rainsoil.core.module.system.vo.SysDictType;
import com.rainsoil.core.module.system.vo.Ztree;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author ruoyi
 */
public interface ISysDictTypeService extends IBaseService<SysDictType> {

	/**
	 * 根据字典类型查询字典数据
	 *
	 * @param dictType 字典类型
	 * @return 字典数据集合信息
	 */
	List<SysDictData> selectDictDataByType(String dictType);


	/**
	 * 加载字典缓存数据
	 */
	void loadingDictCache();

	/**
	 * 清空字典缓存数据
	 */
	void clearDictCache();

	/**
	 * 重置字典缓存数据
	 */
	void resetDictCache();


	/**
	 * 校验字典类型称是否唯一
	 *
	 * @param dictType 字典类型
	 * @return 结果
	 */
	void checkDictTypeUnique(SysDictType dictType);

	/**
	 * 查询字典类型树
	 *
	 * @param dictType 字典类型
	 * @return 所有字典类型
	 */
	List<Ztree> selectDictTree(SysDictType dictType);
}
