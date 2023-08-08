package com.xzl.csdn.auth.model;

import lombok.Data;

/**
 * @author: liupu
 * @description: jwt用户信息
 * @date: 2021/8/9
 */
@Data
public class JwtUser {

	private Integer id;
    private String username;
    private String nickName;

    private Integer loginType;
    private String mobilePhone;
    private String token;

    /**
     * 服务类型
     * 1.WEB服务
     * 2.第三方服务
     */
    private Integer appType;
    /**
     * 登陆版本号，用于后续兼容问题处理
     */
    private Integer version;

    private String townCode;

    private String uuid;

    private Integer unitId;
}
