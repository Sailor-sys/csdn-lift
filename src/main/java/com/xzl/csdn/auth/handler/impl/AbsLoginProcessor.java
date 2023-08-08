package com.xzl.csdn.auth.handler.impl;

import com.xzl.csdn.auth.handler.LoginProcessHandler;
import com.xzl.csdn.auth.model.LoginParamQuery;
import com.xzl.csdn.common.exception.BusinessException;
import com.xzl.csdn.constant.Constants;
import com.xzl.csdn.domain.entity.CsdnUser;

import java.util.Optional;

/**
 * @author XZL
 */
public abstract class AbsLoginProcessor implements LoginProcessHandler {

    @Override
    public void postCheck(LoginParamQuery loginParamQuery, CsdnUser csdnUser) {
        Integer enableFlag = csdnUser.getEnable();
        if (Constants.USER_ENBLE_0.equals(enableFlag)) {
            throw new BusinessException("用户被禁用");
        }
    }


    /**
     * 加载用户基础信息
     */
    protected abstract CsdnUser loadBasic(LoginParamQuery loginParamQuery);

    @Override
    public CsdnUser load(LoginParamQuery loginParamQuery) {
        CsdnUser loginInfo = Optional.ofNullable(loadBasic(loginParamQuery)).orElseThrow(() -> new BusinessException("找不到该用户或用户名密码错误"));
        afterBasicLoad(loginInfo, loginParamQuery);
        return loginInfo;
    }


    protected void afterBasicLoad(CsdnUser csdnUser, LoginParamQuery loginParamQuery) {

    }

}
