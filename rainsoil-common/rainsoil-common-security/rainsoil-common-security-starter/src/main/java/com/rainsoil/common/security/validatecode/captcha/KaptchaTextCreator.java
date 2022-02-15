package com.rainsoil.common.security.validatecode.captcha;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.util.Random;

/**
 * 验证码生成器
 *
 * @author luyanan
 * @since 2021/10/5
 **/
public class KaptchaTextCreator extends DefaultTextCreator {

	private static final String[] CNUMBERS = "0,1,2,3,4,5,6,7,8,9,10".split(",");

	/**
	 * randomoperands
	 *
	 * @since 2022/2/9
	 */

	private static final int RANDOM_OPERANDS = 2;

	@Override
	public String getText() {
		Integer result = 0;
		Random random = new Random();
		int x = random.nextInt(10);
		int y = random.nextInt(10);
		StringBuilder suChinese = new StringBuilder();
		int randomoperands = (int) Math.round(Math.random() * RANDOM_OPERANDS);
		if (randomoperands == 0) {
			result = x * y;
			suChinese.append(CNUMBERS[x]);
			suChinese.append("*");
			suChinese.append(CNUMBERS[y]);
		} else if (randomoperands == 1) {
			if (!(x == 0) && y % x == 0) {
				result = y / x;
				suChinese.append(CNUMBERS[y]);
				suChinese.append("/");
				suChinese.append(CNUMBERS[x]);
			} else {
				result = x + y;
				suChinese.append(CNUMBERS[x]);
				suChinese.append("+");
				suChinese.append(CNUMBERS[y]);
			}
		} else if (randomoperands == RANDOM_OPERANDS) {
			if (x >= y) {
				result = x - y;
				suChinese.append(CNUMBERS[x]);
				suChinese.append("-");
				suChinese.append(CNUMBERS[y]);
			} else {
				result = y - x;
				suChinese.append(CNUMBERS[y]);
				suChinese.append("-");
				suChinese.append(CNUMBERS[x]);
			}
		} else {
			result = x + y;
			suChinese.append(CNUMBERS[x]);
			suChinese.append("+");
			suChinese.append(CNUMBERS[y]);
		}
		suChinese.append("=?@" + result);
		return suChinese.toString();
	}

}