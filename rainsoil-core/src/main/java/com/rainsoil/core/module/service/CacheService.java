//package com.rainsoil.core.module.service;
//
//import com.sun.deploy.util.ArrayUtil;
//import org.apache.commons.lang3.ArrayUtils;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * 缓存操作处理
// *
// * @author ruoyi
// */
//@Service
//public class CacheService {
//	/**
//	 * 获取所有缓存名称
//	 *
//	 * @return 缓存列表
//	 */
//	public String[] getCacheNames() {
//		Collection<String> cacheNames = CacheManagerFactory.cacheManager().getCacheNames();
//		return ArrayUtils.removeElement(ArrayUtil.toArray(cacheNames, String.class), Constants.SYS_AUTH_CACHE);
//	}
//
//	/**
//	 * 根据缓存名称获取所有键名
//	 *
//	 * @param cacheName 缓存名称
//	 * @return 键名列表
//	 */
//	public Set<String> getCacheKeys(String cacheName) {
//		return CacheManagerFactory.getCache(cacheName).keys().stream().map(a -> a.toString()).collect(Collectors.toSet());
//	}
//
//	/**
//	 * 根据缓存名称和键名获取内容值
//	 *
//	 * @param cacheName 缓存名称
//	 * @param cacheKey  键名
//	 * @return 键值
//	 */
//	public Object getCacheValue(String cacheName, String cacheKey) {
//		return CacheManagerFactory.getCache(cacheName).get(cacheKey);
//	}
//
//	/**
//	 * 根据名称删除缓存信息
//	 *
//	 * @param cacheName 缓存名称
//	 */
//	public void clearCacheName(String cacheName) {
//		CacheManagerFactory.getCache(cacheName).clear();
//	}
//
//	/**
//	 * 根据名称和键名删除缓存信息
//	 *
//	 * @param cacheName 缓存名称
//	 * @param cacheKey  键名
//	 */
//	public void clearCacheKey(String cacheName, String cacheKey) {
//
//		CacheManagerFactory.getCache(cacheName).evict(cacheKey);
//	}
//
//	/**
//	 * 清理所有缓存
//	 */
//	public void clearAll() {
//		String[] cacheNames = getCacheNames();
//		for (String cacheName : cacheNames) {
//			CacheManagerFactory.getCache(cacheName).clear();
//		}
//	}
//}
