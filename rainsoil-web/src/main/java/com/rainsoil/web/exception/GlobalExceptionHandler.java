//package com.rainsoil.web.exception;
//
//
//import com.rainsoil.common.core.page.RespEntity;
//import com.rainsoil.common.framework.spring.SpringContextHolder;
//import com.rainsoil.core.exception.BaseException;
//import com.rainsoil.core.message.GlobalCode;
//import com.rainsoil.core.utils.MessageUtils;
//import com.rainsoil.system.exception.SystemException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.validation.BindException;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 全局异常处理器
// *
// * @author ruoyi
// */
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//	@ExceptionHandler(AccessDeniedException.class)
//	public Object AccessDeniedException(AccessDeniedException e, HttpServletRequest request, HandlerMethod handlerMethod) {
//		String requestURI = request.getRequestURI();
//		log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
//		if (SpringContextHolder.isAjaxRequest(request)) {
//			return RespEntity.error(GlobalCode.BUSINESS_UNAUTHORIZED, MessageUtils.message(GlobalCode.BUSINESS_UNAUTHORIZED + ""));
//		} else {
//			return new ModelAndView("error/unauth");
//		}
//	}
//
//	/**
//	 * 请求方式不支持
//	 */
//	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//	public RespEntity handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
//														  HttpServletRequest request) {
//		String requestURI = request.getRequestURI();
//		log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
//		return RespEntity.error(GlobalCode.INTERNAL_SERVER_ERROR, e.getMessage());
//	}
//
//	/**
//	 * 拦截SystemException
//	 *
//	 * @param e
//	 * @param request
//	 * @return RespEntity
//	 * @since 2021/9/23
//	 */
//	@ExceptionHandler(SystemException.class)
//	public RespEntity systemException(SystemException e, HttpServletRequest request) {
//		String requestURI = request.getRequestURI();
//		log.error("请求地址'{}',发生未知异常.", requestURI, e);
//		return RespEntity.error(e.getCode(), e.getMessage());
//
//	}
//
//	/**
//	 * 拦截未知的运行时异常
//	 */
//	@ExceptionHandler(BaseException.class)
//	public RespEntity baseException(BaseException e, HttpServletRequest request) {
//		String requestURI = request.getRequestURI();
//		log.error("请求地址'{}',发生未知异常.", requestURI, e);
//		return RespEntity.error(e.getCode(), e.getMessage());
//	}
//
//	/**
//	 * 拦截未知的运行时异常
//	 */
//	@ExceptionHandler(RuntimeException.class)
//	public RespEntity handleRuntimeException(RuntimeException e, HttpServletRequest request) {
//		String requestURI = request.getRequestURI();
//		log.error("请求地址'{}',发生未知异常.", requestURI, e);
//		return RespEntity.error(GlobalCode.INTERNAL_SERVER_ERROR, e.getMessage());
//	}
//
//	/**
//	 * 系统异常
//	 */
//	@ExceptionHandler(Exception.class)
//	public RespEntity handleException(Exception e, HttpServletRequest request) {
//		String requestURI = request.getRequestURI();
//		log.error("请求地址'{}',发生系统异常.", requestURI, e);
//		return RespEntity.error(GlobalCode.INTERNAL_SERVER_ERROR, e.getMessage());
//	}
//
////	/**
////	 * 业务异常
////	 */
////	@ExceptionHandler(ServiceException.class)
////	public Object handleServiceException(ServiceException e, HttpServletRequest request) {
////		log.error(e.getMessage(), e);
////		if (SpringContextHolder.isAjaxRequest(request)) {
////			return RespEntity.error(GlobalCode.INTERNAL_SERVER_ERROR, e.getMessage());
////		} else {
////			return new ModelAndView("error/service", "errorMessage", e.getMessage());
////		}
////	}
//
//	/**
//	 * 自定义验证异常
//	 */
//	@ExceptionHandler(BindException.class)
//	public RespEntity handleBindException(BindException e) {
//		log.error(e.getMessage(), e);
//		String message = e.getAllErrors().get(0).getDefaultMessage();
//		return RespEntity.error(GlobalCode.INTERNAL_SERVER_ERROR, message);
//	}
//
//
//}
