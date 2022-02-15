package com.rainsoil.common.framework.spring;


import cn.hutool.core.lang.Assert;
import cn.hutool.cron.CronConfig;
import com.rainsoil.common.framework.spring.cors.CorsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringContextHolderTest {

	@Test
	public void testGetBean() {
		CorsConfig corsConfig = SpringContextHolder.getBean(CorsConfig.class);
		Assert.notNull(corsConfig);
	}
}