package com.rainsoil.common.data.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;

import java.util.List;

/**
 * 自定义SqlInjector
 *
 * @author luyanan
 * @since 2022/2/9
 **/
public class MyLogicSqlInjector extends DefaultSqlInjector {

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
		List<AbstractMethod> methodList = super.getMethodList(mapperClass);

		// 根据 id 逻辑删除数据,并带字段填充功能
		methodList.add(new LogicDeleteByIdWithFill());

		return methodList;
	}

}
