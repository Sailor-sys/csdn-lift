package com.xzl.csdn.auth.service;

import java.util.List;

/**
 * @author: liupu
 * @description: 过滤url
 * @date: 2021/8/9
 */
public interface IgnoreUrlService {

    /**
     * get 方法 需过滤url
     */
    List<String> methodGetIgnoreUrl();

    /**
     * 所有过滤url
     */
    List<String> allMethodIgnoreUrl();
}
