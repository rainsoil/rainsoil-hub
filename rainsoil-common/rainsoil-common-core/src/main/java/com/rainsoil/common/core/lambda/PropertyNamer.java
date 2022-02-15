package com.rainsoil.common.core.lambda;

import java.util.Locale;

/**
 * @author luyanan
 * @since 2021/3/8
 **/
public class PropertyNamer {

	private PropertyNamer() {
		// Prevent Instantiation of Static Class
	}

	private static final String IS = "is";

	private static final String SET = "set";

	private static final String GET = "get";

	public static String methodToProperty(String name) {
		if (name.startsWith(IS)) {
			name = name.substring(2);
		}
		else if (name.startsWith(GET) || name.startsWith(SET)) {
			name = name.substring(3);
		}
		else {
			throw new RuntimeException(
					"Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
		}

		boolean flg = name.length() > 1 && !Character.isUpperCase(name.charAt(1));
		if (name.length() == 1 || flg) {
			name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
		}

		return name;
	}

	public static boolean isProperty(String name) {
		return isGetter(name) || isSetter(name);
	}

	public static boolean isGetter(String name) {
		return (name.startsWith(GET) && name.length() > 3) || (name.startsWith(IS) && name.length() > 2);
	}

	public static boolean isSetter(String name) {
		return name.startsWith(SET) && name.length() > 3;
	}

}
