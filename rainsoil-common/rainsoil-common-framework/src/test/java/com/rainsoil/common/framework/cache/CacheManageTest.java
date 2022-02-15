package com.rainsoil.common.framework.cache;

import cn.hutool.core.lang.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 缓存测试类
 *
 * @author luyanan
 * @since 2022/2/8
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class CacheManageTest {

	@Autowired
	private CacheManager cacheManager;


	@Test
	public void cache() {
		String key = "aaa";
		String value = "aaaa";
		Cache cache = cacheManager.getCache(key);
		cache.putIfAbsent(key, value);
		Assert.isTrue(cache.get("aaa").get().toString().equals(value));
	}
}
