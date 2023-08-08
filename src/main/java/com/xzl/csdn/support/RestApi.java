package com.xzl.csdn.support;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * @author：lianp 用来将controller返回值用{@link com.xzl.xcx.meta.ApiResult}包裹
 * @date：11:06 2018/4/12
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
public @interface RestApi {
    //为@RequestMapping换个名称：RestApi，其中@RequestMapping(path="/test",method=RequestMethod.GET)
    //可以改成@RestApi(value="/test",method=RequestMethod.GET)
    @AliasFor(annotation = RequestMapping.class, value = "path") String[] value() default {};

    @AliasFor(annotation = RequestMapping.class, value = "method") RequestMethod[] method() default {};

    /**
     * 是否包裹到result字段中
     * <p>
     * 原本:
     * [1,2,3]
     * <p>
     * 包裹后:
     * {result:[1,2,3]}
     */
    //定义另一个参数wrapResult,类型boolean
    boolean wrapResult() default false;
}
