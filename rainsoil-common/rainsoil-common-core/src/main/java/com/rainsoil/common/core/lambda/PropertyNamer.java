package com.rainsoil.common.core.lambda;

import java.util.Locale;

/**
 * @author luyanan
 * @since 2021/3/8
 **/
public final class PropertyNamer {

	private PropertyNamer() {
		// Prevent Instantiation of Static Class
	}

	/**
	 * is
	 *
	 * @since 2022/3/2
	 */

	private static final String IS = "is";

	/**
	 * set
	 *
	 * @since 2022/3/2
	 */

	private static final String SET = "set";

	/**
	 * get
	 *
	 * @since 2022/3/2
	 */

	private static final String GET = "get";


	/**
	 * 方法名转换为字段
	 *
	 * @param name 方法名
	 * @return java.lang.String
	 * @since 2022/3/2
	 */
	public static String methodToProperty(String name) {
		if (name.startsWith(IS)) {
			name = name.substring(2);
		} else if (name.startsWith(GET) || name.startsWith(SET)) {
			name = name.substring(3);
		} else {
			throw new RuntimeException(
					"Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
		}

		boolean flg = name.length() > 1 && !Character.isUpperCase(name.charAt(1));
		if (name.length() == 1 || flg) {
			name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
		}

		return name;
	}

	/**
	 * 是否为方法名
	 *
	 * @param name 方法名
	 * @return boolean
	 * @since 2022/3/2
	 */
	public static boolean isProperty(String name) {
		return isGetter(name) || isSetter(name);
	}

	/**
	 * 是否为get方法名
	 *
	 * @param name 方法名
	 * @return boolean
	 * @since 2022/3/2
	 */
	public static boolean isGetter(String name) {
		return (name.startsWith(GET) && name.length() > 3) || (name.startsWith(IS) && name.length() > 2);
	}

	/**
	 * 是否为set 方法名
	 *
	 * @param name 方法名
	 * @return boolean
	 * @since 2022/3/2
	 */
	public static boolean isSetter(String name) {
		return name.startsWith(SET) && name.length() > 3;
	}

}
