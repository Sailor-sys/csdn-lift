package com.xzl.csdn.support;

/**
 * @author：lianp
 * @description：
 * @date：10:48 2019/7/22
 */
public interface ObjectConverter<T, V> {
    /**
     * Convert Object
     */
    T convert(V object);
}
