package com.xzl.csdn.auth.model;

import lombok.Data;

/**
 * @author: liupu
 * @description:
 * @date: 2021/8/9
 */
@Data
public class CsdnUserVO {

    private String username;

    private String phoneNo;

    private String userId;

    private String nickName;

	private Integer unitId;
	private Integer loginTag;

    private Integer modifyPwdFlag;

    /**
     * jwt token
     */
    private String token;

	/**
	 * 密码过期 0未过期，1过期
	 */
	private Integer expirePassWordflag = 0;

	/**
	 * 密码过期
	 */
	private String expirePassWordContent;

	private boolean userIsAdmin;

	/**
	 * 实时监控正色，0反色，1正色
	 */
	private Integer actualPureColor;

	/**
	 * 监控回放正色，0反色，1正色
	 */
	private Integer playbackPureColor;
}
