package com.rainsoil.web.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.rainsoil.common.security.core.core.LoginUserDetail;
import com.rainsoil.common.security.core.core.LoginUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自定义字段填充
 *
 * @author luyanan
 * @since 2021/9/21
 **/
@AllArgsConstructor
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

	private final LoginUserService loginUserService;

	/**
	 * 插入元对象字段填充（用于插入时对公共字段的填充）
	 *
	 * @param metaObject 元对象
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		try {
			LoginUserDetail user = loginUserService.getUser(false);
			if (null != user) {
				this.strictInsertFill(metaObject, "createBy", () -> user.getUserId() + "", String.class);
				this.strictInsertFill(metaObject, "updateBy", () -> user.getUserId() + "", String.class);
			}
		} catch (Exception e) {
		}
		this.strictInsertFill(metaObject, "createTime", () -> new Date(), Date.class);
		this.strictInsertFill(metaObject, "updateTime", () -> new Date(), Date.class);
	}

	/**
	 * 更新元对象字段填充（用于更新时对公共字段的填充）
	 *
	 * @param metaObject 元对象
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		try {
			LoginUserDetail user = loginUserService.getUser(false);
			if (null != user) {
				this.strictInsertFill(metaObject, "updateBy", () -> user.getUserId() + "", String.class);
			}
		} catch (Exception e) {
		}
		this.strictInsertFill(metaObject, "updateTime", () -> new Date(), Date.class);
	}
}
