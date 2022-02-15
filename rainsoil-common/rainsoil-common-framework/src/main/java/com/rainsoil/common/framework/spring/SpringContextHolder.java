package com.rainsoil.common.framework.spring;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.core.constants.HttpConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring工具类
 *
 * @author luyanan
 * @since 2021/8/22
 **/
@Slf4j
@Lazy(false)
@Configuration(proxyBeanMethods = false)
public class SpringContextHolder extends org.springframework.web.util.WebUtils
		implements ApplicationContextAware, DisposableBean, BeanFactoryPostProcessor {

	/**
	 * applicationContext
	 *
	 * @since 2022/2/8
	 */

	private static ApplicationContext APPLICATION_CONTEXT = null;

	/**
	 * "@PostConstruct"注解标记的类中，由于ApplicationContext还未加载，导致空指针<br>
	 * 因此实现BeanFactoryPostProcessor注入ConfigurableListableBeanFactory实现bean的操作
	 */
	private static ConfigurableListableBeanFactory BEAN_FACTORY;

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 *
	 * @return org.springframework.context.ApplicationContext
	 * @since 2021/1/23
	 */
	public static ApplicationContext getApplicationContext() {
		return APPLICATION_CONTEXT;
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 *
	 * @since 2021/1/23
	 */
	public static void clearHolder() {
		if (log.isDebugEnabled()) {
			log.debug("清除SpringContextHolder中的ApplicationContext:" + APPLICATION_CONTEXT);
		}
		APPLICATION_CONTEXT = null;
	}

	/**
	 * 从静态变量applicationContext 中取得Bean, 自动转型为所赋值的对象的类型
	 *
	 * @param <T>  泛型
	 * @param name bean的名称
	 * @return T bean对象
	 * @since 2021/1/23
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		if (null == APPLICATION_CONTEXT) {
			return null;
		}
		return (T) APPLICATION_CONTEXT.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 *
	 * @param <T>          泛型
	 * @param requiredType bean class
	 * @return T
	 * @since 2021/1/23
	 */
	public static <T> T getBean(Class<T> requiredType) {
		if (null == APPLICATION_CONTEXT) {
			return null;
		}
		return APPLICATION_CONTEXT.getBean(requiredType);
	}

	/**
	 * 发布事件
	 *
	 * @param event 事件
	 * @since 2021/1/23
	 */
	public static void publishEvent(ApplicationEvent event) {
		if (APPLICATION_CONTEXT == null) {
			return;
		}
		APPLICATION_CONTEXT.publishEvent(event);
	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 *
	 * @since 2021/1/23
	 */
	@Override
	public void destroy() throws Exception {
		SpringContextHolder.clearHolder();
	}

	/**
	 * 实现ApplicationContextAware 接口, 注入到Context的静态变量中
	 *
	 * @param applicationContext
	 * @since 2021/1/23
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.APPLICATION_CONTEXT = applicationContext;
	}

	/**
	 * 获取HttpServletRequest
	 *
	 * @return javax.servlet.http.HttpServletRequest
	 * @since 2021/8/22
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes());
		if (null == servletRequestAttributes) {
			return null;
		}
		return null == servletRequestAttributes.getRequest() ? null : servletRequestAttributes.getRequest();
	}

	/**
	 * 获取HttpServletResponse
	 *
	 * @return javax.servlet.http.HttpServletResponse
	 * @since 2021/8/22
	 */
	public static HttpServletResponse getResponse() {
		ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes());
		if (null == servletRequestAttributes) {
			return null;
		}
		return null == servletRequestAttributes.getResponse() ? null : servletRequestAttributes.getResponse();
	}

	/**
	 * 设置cookie
	 *
	 * @param response        返回对象
	 * @param name            cookie 名称
	 * @param value           cookie value
	 * @param maxAgeInSeconds 存活时间
	 * @param domain          cookie域
	 * @since 2021/1/23
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String domain,
								 int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		if (StrUtil.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}

	/**
	 * 设置cookie
	 *
	 * @param response        返回对象
	 * @param name            cookie 名称
	 * @param value           cookie value
	 * @param maxAgeInSeconds 存活时间
	 * @since 2021/1/23
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		setCookie(response, name, value, null, maxAgeInSeconds);
	}

	/**
	 * 删除cookie
	 *
	 * @param response http 返回对象
	 * @param name     cookie name
	 * @param domain   cookie域
	 * @since 2021/1/23
	 */
	public static void removeCookie(HttpServletResponse response, String name, String domain) {
		setCookie(response, name, null, domain, 0);
	}

	/**
	 * 获取cookie的值
	 *
	 * @param request http request对象
	 * @param name    cookie的名称
	 * @return java.lang.String
	 * @since 2021/1/23
	 */
	public static String getCookieVal(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		return cookie != null ? cookie.getValue() : null;
	}

	/**
	 * 是否返回的是json请求, 返回含有 ResponseBody 或者 RestController注解
	 *
	 * @param handlerMethod handlerMethod
	 * @return boolean
	 * @since 2021/1/23
	 */
	public static boolean isBody(HandlerMethod handlerMethod) {
		return (AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResponseBody.class) != null)
				|| (AnnotationUtils.findAnnotation(handlerMethod.getBean().getClass(), RestController.class) != null);
	}

	/**
	 * 获取HandlerMethod
	 *
	 * @param request 请求体
	 * @return org.springframework.web.method.HandlerMethod
	 * @since 2021/6/6
	 */
	@SneakyThrows
	public static HandlerMethod getHandlerMethod(HttpServletRequest request) {
		RequestMappingHandlerMapping requestMappingHandlerMapping = APPLICATION_CONTEXT
				.getBean(RequestMappingHandlerMapping.class);
		Object handler = requestMappingHandlerMapping.getHandler(request).getHandler();
		return (handler instanceof HandlerMethod) ? (HandlerMethod) handler : null;
	}

	/**
	 * 获取HandlerMethod
	 *
	 * @return org.springframework.web.method.HandlerMethod
	 * @since 2021/6/6
	 */
	@SneakyThrows
	public static HandlerMethod getHandlerMethod() {
		return getHandlerMethod(getRequest());
	}

	/**
	 * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
	 *
	 * @param name bean 名称
	 * @return boolean
	 */
	public static boolean containsBean(String name) {
		return getBeanFactory().containsBean(name);
	}

	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	 * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 *
	 * @param name bean 名称
	 * @return boolean
	 * @throws NoSuchBeanDefinitionException 没有找见bean 异常
	 */
	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return getBeanFactory().isSingleton(name);
	}

	/**
	 * @param name bean 名称
	 * @return Class 注册对象的类型
	 * @throws NoSuchBeanDefinitionException 没有找见bean异常
	 */
	public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return getBeanFactory().getType(name);
	}


	/**
	 * 如果给定的bean名字在bean定义中有别名，则返回这些别名
	 *
	 * @param name bean 名称
	 * @return java.lang.String[]
	 * @throws NoSuchBeanDefinitionException 没有找见bean异常
	 * @since 2022/2/8
	 */
	public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
		return getBeanFactory().getAliases(name);
	}

	/**
	 * 获取aop代理对象
	 *
	 * @param invoker bean
	 * @param <T>     泛型
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAopProxy(T invoker) {
		return (T) AopContext.currentProxy();
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		SpringContextHolder.BEAN_FACTORY = beanFactory;
	}

	/**
	 * 获取{@link ListableBeanFactory}，可能为{@link ConfigurableListableBeanFactory} 或
	 * {@link ApplicationContextAware}
	 *
	 * @return {@link ListableBeanFactory}
	 * @since 5.7.0
	 */
	public static ListableBeanFactory getBeanFactory() {
		return null == BEAN_FACTORY ? APPLICATION_CONTEXT : BEAN_FACTORY;
	}

	/**
	 * 获取当前的环境配置，无配置返回null
	 *
	 * @return 当前的环境配置
	 */
	public static String[] getActiveProfiles() {
		return APPLICATION_CONTEXT.getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取当前的环境配置，当有多个环境配置时，只获取第一个
	 *
	 * @return 当前的环境配置
	 */
	public static String getActiveProfile() {
		final String[] activeProfiles = getActiveProfiles();
		return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
	}

	/**
	 * 将字符串渲染到客户端
	 *
	 * @param response 渲染对象
	 * @param string   待渲染的字符串
	 * @return null
	 */
	public static String renderString(HttpServletResponse response, String string) {
		try {
			response.setContentType(HttpConstants.APPLICATION_JSON);
			response.setCharacterEncoding(CharsetUtil.UTF_8);
			response.getWriter().print(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 是否是Ajax异步请求
	 *
	 * @param request 请求体
	 * @return boolean
	 * @since 2022/2/8
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String accept = request.getHeader(HttpConstants.ACCEPT);
		if (accept != null && accept.indexOf(HttpConstants.APPLICATION_JSON) != -1) {
			return true;
		}

		String xRequestedWith = request.getHeader(HttpConstants.X_REQUESTED_WITH);
		if (xRequestedWith != null && xRequestedWith.indexOf(HttpConstants.XML_HTTP_REQUEST) != -1) {
			return true;
		}

		String uri = request.getRequestURI();
		if (StrUtil.equalsAnyIgnoreCase(uri, HttpConstants.JSON, HttpConstants.XML)) {
			return true;
		}

		String ajax = request.getParameter(HttpConstants.AJAX);
		if (StrUtil.equalsAnyIgnoreCase(ajax, HttpConstants.JSON, HttpConstants.XML)) {
			return true;
		}
		return false;
	}

	/**
	 * 定义移动端请求的所有可能类型
	 */
	private static final String[] AGENT = {HttpConstants.ANDROID
			, HttpConstants.IPHONE
			, HttpConstants.IPOD
			, HttpConstants.IPAD
			, HttpConstants.WINDOWS_PHONE
			, HttpConstants.MQQBROWSER};

	/**
	 * Windows NT
	 *
	 * @since 2022/2/8
	 */

	private static final String WINDOWS_NT = "Windows NT";

	/**
	 * compatible
	 *
	 * @since 2022/2/8
	 */

	private static final String COMPATIBLE = "compatible; MSIE 9.0;";

	/**
	 * Macintosh
	 *
	 * @since 2022/2/8
	 */

	private static final String MACINTOSH = "Macintosh";

	/**
	 * 判断不是window的ua
	 *
	 * @param ua userAgent
	 * @return boolean
	 * @since 2022/2/8
	 */
	private static boolean notWindowUa(String ua) {
		return !ua.contains(WINDOWS_NT)
				|| (ua.contains(WINDOWS_NT)
				&& ua.contains(COMPATIBLE));
	}


	/**
	 * 判断User-Agent 是不是来自于手机
	 *
	 * @param ua userAgent
	 * @return boolean
	 * @since 2022/2/8
	 */
	public static boolean checkAgentIsMobile(String ua) {
		boolean flag = false;
		if (notWindowUa(ua)) {
			// 排除 苹果桌面系统
			if (!ua.contains(WINDOWS_NT) && !ua.contains(MACINTOSH)) {
				for (String item : AGENT) {
					if (ua.contains(item)) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}

}
