package com.rainsoil.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

/**
 * ObjectMapper 工具类
 *
 * @author luyanan
 * @since 2022/2/15
 **/
@UtilityClass
public class ObjectMapperUtils {


	/**
	 * 获取ObjectMapper对象
	 *
	 * @return com.fasterxml.jackson.databind.ObjectMapper
	 * @since 2022/2/15
	 */
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}

}
