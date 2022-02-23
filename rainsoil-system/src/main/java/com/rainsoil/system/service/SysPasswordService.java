package com.rainsoil.system.service;//package com.rainsoil.system.service;
//
//import com.rainsoil.core.constant.ShiroConstants;
//import com.rainsoil.core.module.system.vo.SysUser;
//import com.rainsoil.core.exception.user.UserPasswordNotMatchException;
//import com.rainsoil.core.exception.user.UserPasswordRetryLimitExceedException;
//import org.apache.shiro.cache.Cache;
//import org.apache.shiro.cache.CacheManager;
//import org.apache.shiro.crypto.hash.Md5Hash;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * 登录密码方法
// *
// * @author luyanan
// * @since 2021/10/1
// **/
//@Component
//public class SysPasswordService {
//	@Autowired
//	private CacheManager cacheManager;
//
//	private Cache<String, AtomicInteger> loginRecordCache;
//
//	@Value(value = "${user.password.maxRetryCount}")
//	private String maxRetryCount;
//
//	@PostConstruct
//	public void init() {
//		loginRecordCache = cacheManager.getCache(ShiroConstants.LOGINRECORDCACHE);
//	}
//
//	public void validate(SysUser user, String password) {
//		String loginName = user.getLoginName();
//
//		AtomicInteger retryCount = loginRecordCache.get(loginName);
//
//		if (retryCount == null) {
//			retryCount = new AtomicInteger(0);
//			loginRecordCache.put(loginName, retryCount);
//		}
//		if (retryCount.incrementAndGet() > Integer.valueOf(maxRetryCount).intValue()) {
//			throw new UserPasswordRetryLimitExceedException(Integer.valueOf(maxRetryCount).intValue());
//		}
//
//		if (!matches(user, password)) {
//			loginRecordCache.put(loginName, retryCount);
//			throw new UserPasswordNotMatchException();
//		} else {
//			clearLoginRecordCache(loginName);
//		}
//	}
//
//	public boolean matches(SysUser user, String newPassword) {
//		return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
//	}
//
//	public void clearLoginRecordCache(String loginName) {
//		loginRecordCache.remove(loginName);
//	}
//
//	public String encryptPassword(String loginName, String password, String salt) {
//		return new Md5Hash(loginName + password + salt).toHex();
//	}
//}
