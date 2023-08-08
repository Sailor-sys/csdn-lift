package com.xzl.csdn.auth.handler;


import com.xzl.csdn.auth.model.LoginParamQuery;
import com.xzl.csdn.domain.entity.CsdnUser;

/**
 * @author XZL
 */
public interface LoginProcessHandler {

    /**
     * 登陆方式
     */
    Integer loginType();
    /**
     * 校验
     */
    void beforeCheck(LoginParamQuery loginParamQuery);

    /**
     * 加载用户信息
     */
    CsdnUser load(LoginParamQuery loginParamQuery);

    /**
     * 后置校验
     */
    default void postCheck(LoginParamQuery loginParamQuery, CsdnUser userInfoDO){}

}
