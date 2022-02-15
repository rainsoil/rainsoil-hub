package com.rainsoil.core.message;

/**
 * 全局编码
 *
 * @author luyanan
 * @since 2022/2/15
 **/
public interface GlobalCode {
	/**
	 * 成功
	 *
	 * @since 2021/9/1
	 */

	int SUCCESS = 200;

	/**
	 * 服务器内部错误，无法完成请求
	 *
	 * @since 2021/8/31
	 */

	int INTERNAL_SERVER_ERROR = 500;


	/**
	 * 没有登录,跳转登录
	 *
	 * @since 2021/9/1
	 */

	int UNAUTHORIZED = 401;

	/**
	 * 服务器无法根据客户端的请求找到资源
	 *
	 * @since 2021/9/1
	 */

	int NOT_FOUND = 404;


	/**
	 * 客户端请求中的方法被禁止
	 *
	 * @since 2021/9/1
	 */

	int METHOD_NOT_ALLOWED = 405;


	/**
	 * 没有权限
	 *
	 * @since 2021/9/1
	 */

	int BUSINESS_UNAUTHORIZED = 4001;


	/**
	 * 数据不存在
	 * {}存在,不允许添加
	 *
	 * @since 2021/9/11
	 */

	int DATA_NOT_FOUNT = 4040;


	/**
	 * 数据存在
	 *
	 * @since 2021/9/11
	 */

	int DATA_EXIST = 4002;


	/**
	 * 请求参数错误
	 *
	 * @since 2021/9/21
	 */

	int BAD_REQUEST = 400;
}
