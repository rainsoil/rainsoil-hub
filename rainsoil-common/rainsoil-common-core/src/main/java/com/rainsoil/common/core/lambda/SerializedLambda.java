package com.rainsoil.common.core.lambda;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类是从 {@link java.lang.invoke.SerializedLambda} 里面 copy 过来的， 字段信息完全一样 负责将一个支持序列的
 * Function 序列化为 SerializedLambda
 * </p>
 *
 * @author luyanan
 * @since 2021/3/8
 **/
@SuppressFBWarnings(value = {"OBJECT_DESERIALIZATION"}, justification = "I prefer to suppress these FindBugs warnings")
public class SerializedLambda implements Serializable {

	private static final long serialVersionUID = 8025925345765570181L;

	;

	private Class<?> capturingClass;

	private String functionalInterfaceClass;

	private String functionalInterfaceMethodName;

	private String functionalInterfaceMethodSignature;

	private String implClass;

	private String implMethodName;

	private String implMethodSignature;

	private int implMethodKind;

	private String instantiatedMethodType;

	private Object[] capturedArgs;

	/**
	 * 通过反序列化转换 lambda 表达式，该方法只能序列化 lambda 表达式，不能序列化接口实现或者正常非 lambda 写法的对象
	 *
	 * @param lambda lambda对象
	 * @return 返回解析后的 SerializedLambda
	 */
	public static SerializedLambda resolve(Sfunction<?, ?> lambda) {
		if (!lambda.getClass().isSynthetic()) {
			throw new RuntimeException("该方法仅能传入 lambda 表达式产生的合成类");
		}
		try (ObjectInputStream objIn = new ObjectInputStream(
				new ByteArrayInputStream(SerializationUtils.serialize(lambda))) {
			@Override
			protected Class<?> resolveClass(ObjectStreamClass objectStreamClass)
					throws IOException, ClassNotFoundException {
				Class<?> clazz = super.resolveClass(objectStreamClass);
				return clazz == java.lang.invoke.SerializedLambda.class ? SerializedLambda.class : clazz;
			}
		}) {
			return (SerializedLambda) objIn.readObject();
		} catch (ClassNotFoundException | IOException e) {
			throw new RuntimeException("This is impossible to happen", e);
		}
	}

	/**
	 * 获取接口 class
	 *
	 * @return 返回 class 名称
	 */
	public String getFunctionalInterfaceClassName() {
		return normalName(functionalInterfaceClass);
	}

	/**
	 * 获取实现的 class
	 *
	 * @return 实现类
	 */
	public Class<?> getImplClass() {

		return toClassConfident(getImplClassName());
	}

	/**
	 * 获取 class 的名称
	 *
	 * @return 类名
	 */
	public String getImplClassName() {
		return normalName(implClass);
	}

	/**
	 * 获取实现者的方法名称
	 *
	 * @return 方法名称
	 */
	public String getImplMethodName() {
		return implMethodName;
	}

	/**
	 * 正常化类名称，将类名称中的 / 替换为 .
	 *
	 * @param name 名称
	 * @return 正常的类名
	 */
	private String normalName(String name) {
		return name.replace('/', '.');
	}

	private static final Pattern INSTANTIATED_METHOD_TYPE = Pattern
			.compile("\\(L(?<instantiatedMethodType>[\\S&&[^;)]]+);\\)L[\\S]+;");

	public Class getInstantiatedMethodType() {
		Matcher matcher = INSTANTIATED_METHOD_TYPE.matcher(instantiatedMethodType);
		if (matcher.find()) {
			return toClassConfident(normalName(matcher.group("instantiatedMethodType")));
		}
		throw new RuntimeException("无法从" + instantiatedMethodType + "解析调用实例。。。");
	}

	/**
	 * @return 字符串形式
	 */
	@Override
	public String toString() {
		return String.format("%s -> %s::%s", getFunctionalInterfaceClassName(), getImplClass().getSimpleName(),
				implMethodName);
	}

	/**
	 * <p>
	 * 请仅在确定类存在的情况下调用该方法
	 * </p>
	 *
	 * @param name 类名称
	 * @return 返回转换后的 Class
	 */
	public static Class<?> toClassConfident(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("找不到指定的class！请仅在明确确定会有 class 的时候，调用该方法", e);
		}
	}

}