package com.rainsoil.common.core.lambda;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Locale.ENGLISH;

/**
 * Lamdba工具类
 *
 * @author luyanan
 * @since 2021/3/8
 **/
public class LambdaUtils<T> {

	private Class<T> entityClass;

	private T entity;


	public LambdaUtils(Class<T> entityClass) {
		this.entityClass = entityClass;
		init();
	}

	public LambdaUtils(T entity) {

		this.entity = entity;
		init();
	}

	private void init() {
		if (null == this.entityClass && this.entity != null) {
			this.entityClass = (Class<T>) entity.getClass();
		}

		if (null != this.entityClass) {
			LAMBDA_MAP.put(entityClass.getName(), createColumnCacheMap(this.entityClass));
		}

	}

	private Map<String, ColumnCache> createColumnCacheMap(Class<T> entityClass) {
		Map<String, ColumnCache> map = new HashMap<>(16);
		Arrays.stream(entityClass.getFields()).forEach(i -> {
			map.put(formatKey(i.getName()), new ColumnCache(i.getName(), ""));
		});
		return map;
	}

	/**
	 * 字段映射
	 */
	private static final Map<String, Map<String, ColumnCache>> LAMBDA_MAP = new ConcurrentHashMap<>();

	/**
	 * SerializedLambda 反序列化缓存
	 */
	private static final Map<Class<?>, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>();


	/**
	 * 解析 lambda 表达式, 该方法只是调用了 {@link SerializedLambda#resolve(Sfunction)} 中的方法，在此基础上加了缓存。
	 * 该缓存可能会在任意不定的时间被清除
	 *
	 * @param func 需要解析的 lambda 对象
	 * @param <T>  类型，被调用的 Function 对象的目标类型
	 * @return 返回解析后的结果
	 * @see SerializedLambda#resolve(Sfunction)
	 */
	public static <T> SerializedLambda resolve(Sfunction<T, ?> func) {
		Class<?> clazz = func.getClass();
		return Optional.ofNullable(FUNC_CACHE.get(clazz)).map(WeakReference::get).orElseGet(() -> {
			SerializedLambda lambda = SerializedLambda.resolve(func);
			FUNC_CACHE.put(clazz, new WeakReference<>(lambda));
			return lambda;
		});
	}

	/**
	 * 格式化 key 将传入的 key 变更为大写格式
	 *
	 * <pre>
	 *     Assert.assertEquals("USERID", formatKey("userId"))
	 * </pre>
	 *
	 * @param key key
	 * @return 大写的 key
	 */
	public static String formatKey(String key) {
		return key.toUpperCase(ENGLISH);
	}

	/**
	 * 获取实体对应字段 MAP
	 *
	 * @param clazz 实体类
	 * @return 缓存 map
	 */
	public static Map<String, ColumnCache> getColumnMap(Class<?> clazz) {
		return LAMBDA_MAP.getOrDefault(clazz.getName(), Collections.emptyMap());
	}

	/**
	 * 获取 SerializedLambda 对应的列信息，从 lambda 表达式中推测实体类
	 * <p>
	 * 如果获取不到列信息，那么本次条件组装将会失败
	 *
	 * @param lambda     lambda 表达式
	 * @param onlyColumn 如果是，结果: "name", 如果否： "name" as "name"
	 * @return 列 // * @throws
	 * com.baomidou.mybatisplus.core.exceptions.MybatisPlusException 获取不到列信息时抛出异常
	 * @see SerializedLambda#getImplClass()
	 * @see SerializedLambda#getImplMethodName()
	 */
	private String getColumn(SerializedLambda lambda, boolean onlyColumn) {
		String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
		return fieldName;
	}

	public String columnToString(Sfunction<T, ?> column) {
		return columnToString(column, true);
	}

	public String columnToString(Sfunction<T, ?> column, boolean onlyColumn) {
		return getColumn(LambdaUtils.resolve(column), onlyColumn);
	}

}
