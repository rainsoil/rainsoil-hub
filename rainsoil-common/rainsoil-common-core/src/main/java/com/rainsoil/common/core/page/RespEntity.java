package com.rainsoil.common.core.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应返回
 *
 * @param <T> 泛型
 * @author luyanan
 * @since 2021/8/13
 **/
@Data
@ApiModel("统一响应返回")
public class RespEntity<T> implements Serializable {

	private static final long serialVersionUID = 1996369589258128651L;

	/**
	 * 默认成功状态码
	 *
	 * @since 2022/2/8
	 */

	public static final int SUCCESS_STATUS = 200;

	/**
	 * 返回状态码
	 *
	 * @since 2021/8/13
	 */
	@ApiModelProperty(value = "返回状态码")
	private int status;

	/**
	 * 返回消息
	 *
	 * @since 2021/8/13
	 */
	@ApiModelProperty(value = "返回消息")
	private String msg;

	/**
	 * 响应的数据
	 *
	 * @since 2021/8/13
	 */
	@ApiModelProperty(value = "响应的数据")
	private T data;

	/**
	 * 时间戳
	 *
	 * @since 2021/8/13
	 */
	@ApiModelProperty(value = "时间戳")
	private Long timestamp;

	/**
	 * 成功返回
	 *
	 * @param <T>  泛型
	 * @param data 返回的数据
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.RespEntity<T>
	 * @since 2021/1/24
	 */
	public static <T> RespEntity<T> success(T data) {
		return initSuccessData(data, SUCCESS_STATUS, "ok");
	}

	/**
	 * 成功返回
	 *
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.RespEntity<T>
	 * @since 2021/1/24
	 */
	public static RespEntity success() {
		return initSuccessData(null, SUCCESS_STATUS, "ok");
	}

	/**
	 * 是否成功
	 *
	 * @return boolean
	 * @since 2021/2/28
	 */
	public boolean isSuccess() {
		return this.getStatus() == SUCCESS_STATUS;
	}

	/**
	 * 是否失败
	 *
	 * @return boolean
	 * @since 2021/2/28
	 */
	public boolean isError() {
		return !isSuccess();
	}

	/**
	 * 异常信息
	 *
	 * @param <T>    泛型
	 * @param args   参数
	 * @param status 状态值
	 * @return io.github.fallingsoulm.easy.archetype.framework.core.RespEntity<T>
	 * @since 2021/1/24
	 */
	public static <T> RespEntity<T> error(int status, String args) {
		return initErrorData(status, args);
	}

	/**
	 * 初始化成功的数据
	 *
	 * @param data   数据
	 * @param msg    消息
	 * @param status 状态码
	 * @param <T>    泛型
	 * @return void
	 * @since 2021/2/28
	 */
	private static <T> RespEntity initSuccessData(T data, int status, String msg) {
		RespEntity<T> respEntity = new RespEntity();
		respEntity.setData(data);
		respEntity.setMsg(msg);
		respEntity.setStatus(status);

		return respEntity;
	}


	/**
	 * 初始化异常数据
	 *
	 * @param <T>    泛型
	 * @param status 状态值
	 * @param msg    消息
	 * @return com.rainsoil.fastjava.common.framework.core.RespEntity<T>
	 * @since 2022/2/7
	 */
	private static <T> RespEntity<T> initErrorData(int status, String msg) {
		RespEntity<T> respEntity = new RespEntity();
		respEntity.setMsg(msg);
		respEntity.setStatus(status);
		return respEntity;
	}

}
