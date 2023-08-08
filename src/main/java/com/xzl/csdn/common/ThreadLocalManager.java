package com.xzl.csdn.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shiqh
 * @date 2023-07-25 09:56
 **/
public class ThreadLocalManager {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 存储&绑定
     */
    public static void bind(String key, Object value) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(key, value);
        THREAD_LOCAL.set(map);
    }

    /**
     * 获取绑定数据
     */
    public static Object get(String key) {
        if (THREAD_LOCAL.get() == null) {
            return null;
        }
        return THREAD_LOCAL.get().get(key);
    }

    /**
     * 删除
     */
    public static void remove(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map != null) {
            map.remove(key);
        }
    }
}
