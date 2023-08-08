package com.xzl.csdn.auth.model;

import lombok.Getter;

/**
 * @author: liupu
 * @description: 登录方式
 * @date: 2021/8/9
 */
public enum LoginTypeEnum {

    /**
     * 登陆方式
     */
    DEFAULT(2,"用户名密码普通验证码登陆"),
    MOBILE_PHONE(1,"手机验证码登陆"),
    CLIENT_MODE(3,"其他服务登陆模式"),
    GEE_TEST(4,"图形验证码"),
    SSO(5,"单点登录"),
    ;

    @Getter
    Integer code;
    String desc;

    LoginTypeEnum(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
