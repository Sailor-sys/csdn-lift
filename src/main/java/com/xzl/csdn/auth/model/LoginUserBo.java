package com.xzl.csdn.auth.model;

import lombok.Data;

/**
 * @author：lianp
 * @description：
 * @date：11:49 2018/6/6
 */
@Data
public class LoginUserBo {

    private Integer userId;
    private String username;
    private String nickname;
    private String townCode;
    private String phone;
    private Integer enable;
    private String role;
    private Integer unitId;
    private Integer loginTag;

	/**
	 * 实时监控正色，0反色，1正色
	 */
	private Integer actualPureColor;

	/**
	 * 监控回放正色，0反色，1正色
	 */
	private Integer playbackPureColor;

    @Override
    public boolean equals(Object o){
        if(o==null){
            return false;
        }
        if(o.toString().equals(this.username)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return username.hashCode();
    }

    @Override
    public String toString() {
        return this.username;
    }

    //判断是否验证
    private boolean authenticated=false;

	/**
	 * 密码过期 0未过期，1过期
	 */
	private Integer expirePassWordflag = 0;

	/**
	 * 密码过期
	 */
	private String expirePassWordContent;

    private boolean userIsAdmin;

}
