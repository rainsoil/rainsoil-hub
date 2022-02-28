package com.rainsoil.core.utils;


import com.rainsoil.common.framework.spring.SpringContextHolder;
import com.rainsoil.core.constant.Constants;
import com.rainsoil.core.module.system.vo.SysDictData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典工具类
 *
 * @author ruoyi
 */
@SuppressFBWarnings({"NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"})
@Component
public class DictUtils {


	/**
	 * 分隔符
	 */
	public static final String SEPARATOR = ",";

	/**
	 * 设置字典缓存
	 *
	 * @param key       参数键
	 * @param dictDatas 字典数据列表
	 */
	public static void setDictCache(String key, List<SysDictData> dictDatas) {
		getCache().put(key, dictDatas);

	}

	/**
	 * 获取字典缓存
	 *
	 * @param key 参数键
	 * @return dictDatas 字典数据列表
	 */
	public static List<SysDictData> getDictCache(String key) {
		if (null == key) {
			return new ArrayList<>();
		}

		Cache cache = getCache();
		if (null == cache) {
			return new ArrayList<>();
		}
		Object cacheObj = cache.get(key).get();
		if (StringUtil.isNotNull(cacheObj)) {
			List<SysDictData> dictData = StringUtil.cast(cacheObj);
			return dictData;
		}
		return null;
	}

	/**
	 * 根据字典类型和字典值获取字典标签
	 *
	 * @param dictType  字典类型
	 * @param dictValue 字典值
	 * @return 字典标签
	 */
	public static String getDictLabel(String dictType, String dictValue) {
		return getDictLabel(dictType, dictValue, SEPARATOR);
	}

	/**
	 * 根据字典类型和字典标签获取字典值
	 *
	 * @param dictType  字典类型
	 * @param dictLabel 字典标签
	 * @return 字典值
	 */
	public static String getDictValue(String dictType, String dictLabel) {
		return getDictValue(dictType, dictLabel, SEPARATOR);
	}

	/**
	 * 根据字典类型和字典值获取字典标签
	 *
	 * @param dictType  字典类型
	 * @param dictValue 字典值
	 * @param separator 分隔符
	 * @return 字典标签
	 */
	public static String getDictLabel(String dictType, String dictValue, String separator) {
		StringBuilder propertyString = new StringBuilder();
		List<SysDictData> datas = getDictCache(dictType);

		if (StringUtil.containsAny(separator, dictValue) && StringUtil.isNotEmpty(datas)) {
			for (SysDictData dict : datas) {
				for (String value : dictValue.split(separator)) {
					if (value.equals(dict.getDictValue())) {
						propertyString.append(dict.getDictLabel() + separator);
						break;
					}
				}
			}
		} else {
			for (SysDictData dict : datas) {
				if (dictValue.equals(dict.getDictValue())) {
					return dict.getDictLabel();
				}
			}
		}
		return StringUtil.stripEnd(propertyString.toString(), separator);
	}

	/**
	 * 根据字典类型和字典标签获取字典值
	 *
	 * @param dictType  字典类型
	 * @param dictLabel 字典标签
	 * @param separator 分隔符
	 * @return 字典值
	 */
	public static String getDictValue(String dictType, String dictLabel, String separator) {
		StringBuilder propertyString = new StringBuilder();
		List<SysDictData> datas = getDictCache(dictType);

		if (StringUtil.containsAny(separator, dictLabel) && StringUtil.isNotEmpty(datas)) {
			for (SysDictData dict : datas) {
				for (String label : dictLabel.split(separator)) {
					if (label.equals(dict.getDictLabel())) {
						propertyString.append(dict.getDictValue() + separator);
						break;
					}
				}
			}
		} else {
			for (SysDictData dict : datas) {
				if (dictLabel.equals(dict.getDictLabel())) {
					return dict.getDictValue();
				}
			}
		}
		return StringUtil.stripEnd(propertyString.toString(), separator);
	}

	/**
	 * 删除指定字典缓存
	 *
	 * @param key 字典键
	 */
	public static void removeDictCache(String key) {
		getCache().evict(key);
	}

	/**
	 * 清空字典缓存
	 */
	public static void clearDictCache() {
		getCache().clear();
	}

	/**
	 * 获取cache name
	 *
	 * @return 缓存名
	 */
	public static String getCacheName() {
		return Constants.SYS_DICT_CACHE;
	}

	/**
	 * 设置cache key
	 *
	 * @param configKey 参数键
	 * @return 缓存键key
	 */
	public static String getCacheKey(String configKey) {
		return Constants.SYS_DICT_KEY + configKey;
	}

	private static Cache getCache() {
		CacheManager cacheManager = SpringContextHolder.getBean(CacheManager.class);
		if (null == cacheManager) {
			throw new NullPointerException("cacheManager  is null");
		}
		String cacheName = getCacheName();
		if (null == cacheManager) {
			throw new NullPointerException("cacheName is null");
		}
		Cache cache = cacheManager.getCache(cacheName);
		if (null == cache) {
			throw new NullPointerException("cache is null");
		}
		return cache;
	}
}
