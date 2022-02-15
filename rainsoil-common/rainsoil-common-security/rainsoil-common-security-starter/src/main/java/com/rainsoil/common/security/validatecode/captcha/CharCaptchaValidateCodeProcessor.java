package com.rainsoil.common.security.validatecode.captcha;

import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.Producer;
import com.rainsoil.common.security.validatecode.AbstractValidateCodeProcessor;
import com.rainsoil.common.security.validatecode.exception.ValidateCodeErrorException;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 字符图形验证码生成
 *
 * @author luyanan
 * @since 2021/10/5
 **/
public class CharCaptchaValidateCodeProcessor extends AbstractValidateCodeProcessor {

	@Resource(name = "captchaProducer")
	private Producer captchaProducer;

	/**
	 * 生成验证码
	 * @param key
	 * @return java.lang.String
	 * @since 2021/10/5
	 */
	@Override
	protected ValidateCodeResultHandler doCreate(String key) {
		// 生成验证码
		String code = captchaProducer.createText();
		BufferedImage image = captchaProducer.createImage(code);
		// 转换流信息写出
		FastByteArrayOutputStream os = new FastByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", os);
		}
		catch (IOException e) {
			throw new ValidateCodeErrorException("验证码生成失败");
		}
		// 将结果写出去

		return new ValidateCodeResultHandler() {
			@Override
			public String base64() {
				return Base64.encode(os.toByteArray());
			}

			@Override
			public String code() {
				return code;
			}
		};
	}

	/**
	 * 验证码的类型
	 * @return java.lang.String
	 * @since 2021/10/4
	 */
	@Override
	public String type() {
		return ValidateCodeConstant.TYPE_CHAR;
	}

}
