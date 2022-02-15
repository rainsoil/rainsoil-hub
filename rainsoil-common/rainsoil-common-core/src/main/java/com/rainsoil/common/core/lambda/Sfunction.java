package com.rainsoil.common.core.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 支持序列化的Function
 *
 * @author luyanan
 * @since 2021/3/8
 **/
@FunctionalInterface
public interface Sfunction<T, R> extends Function<T, R>, Serializable {

}
