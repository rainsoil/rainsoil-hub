package com.rainsoil.common.security.validatecode;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.rainsoil.common.security.core.validatecode.storage.ValidateCodeStorage;
import com.rainsoil.common.security.validatecode.exception.ValidateCodeErrorException;
import com.rainsoil.common.security.validatecode.exception.ValidateCodeExpireException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

/**
 * 验证码生成抽象类
 *
 * @author luyanan
 * @since 2021/10/5
 **/
public abstract class AbstractValidateCodeProcessor implements ValidateCodeProcessor {

	@Autowired
	private ValidateCodeStorage validateCodeStorage;

	/**
	 * 验证码生成
	 *
	 * @param key     key
	 * @param timeout 失效时间
	 * @return java.lang.String base64
	 * @since 2021/10/4
	 */
	@Override
	public ValidateCodeResultHandler create(String key, Duration timeout) {
		Assert.notBlank(key, "key cannot be empty");
		Assert.notNull(timeout, "time cannot be empty ");
		ValidateCodeResultHandler code = doCreate(key);
		Assert.notNull(code, "code cannot be empty");
		validateCodeStorage().storage(key, code.code(), timeout);
		return code;
	}

	/**
	 * 生成验证码
	 *
	 * @param key key
	 * @return java.lang.String
	 * @since 2021/10/5
	 */
	@ApiOperation(value = "生成验证码")
	protected abstract ValidateCodeResultHandler doCreate(String key);

	/**
	 * 验证码校验
	 *
	 * @param key  key
	 * @param code 验证码
	 * @since 2021/10/4
	 */
	@Override
	public void validate(String key, String code) throws ValidateCodeExpireException, ValidateCodeErrorException {

		String storageCode = validateCodeStorage().getCode(key);
		if (StrUtil.isBlank(storageCode)) {
			throw new ValidateCodeExpireException("验证码已经过期");
		}
		if (!code.equalsIgnoreCase(storageCode)) {
			throw new ValidateCodeErrorException("验证码不正确");
		}
	}

	@Override
	public ValidateCodeStorage validateCodeStorage() {
		return validateCodeStorage;
	}

}
