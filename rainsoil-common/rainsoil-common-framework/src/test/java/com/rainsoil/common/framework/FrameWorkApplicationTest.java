package com.rainsoil.common.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * framework启动测试类
 *
 * @author luyanan
 * @since 2022/2/8
 **/
@EnableCaching
@SpringBootApplication
public class FrameWorkApplicationTest {

	public static void main(String[] args) {
		SpringApplication.run(FrameWorkApplicationTest.class, args);
	}
}
