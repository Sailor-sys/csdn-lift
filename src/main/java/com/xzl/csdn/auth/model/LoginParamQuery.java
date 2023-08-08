package com.xzl.csdn.auth.model;

import lombok.Data;

/**
 * @author: liupu
 * @description: 登录参数
 * @date: 2021/8/9
 */
@Data
public class LoginParamQuery {
    private String username;
    private String password;

    private String imageCode;
    private String imageCodeKey;

    /**
     * 默认为用户名密码登陆
     */
    private Integer loginType;

    private String ip;

    private String jwtToken;

    private Integer unitType;

	/**
	 * 登录平台，1-救援平台
	 */
	private Integer platformType;

	private String appCode;
}
