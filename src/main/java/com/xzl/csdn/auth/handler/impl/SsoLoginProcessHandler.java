package com.xzl.csdn.auth.handler.impl;

import com.google.common.base.Strings;
import com.xzl.csdn.auth.model.LoginParamQuery;
import com.xzl.csdn.auth.model.LoginTypeEnum;
import com.xzl.csdn.common.exception.BusinessException;
import com.xzl.csdn.domain.entity.CsdnUser;
import com.xzl.csdn.service.CsdnUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SsoLoginProcessHandler extends AbsLoginProcessor {

    @Autowired
    private CsdnUserService csdnUserService;

    @Override
    public Integer loginType() {
        return LoginTypeEnum.SSO.getCode();
    }

    @Override
    public void beforeCheck(LoginParamQuery loginParamQuery) {
        if (Strings.isNullOrEmpty(loginParamQuery.getUsername())) {
            throw new BusinessException("用户名不能为空");
        }
    }

    @Override
    protected CsdnUser loadBasic(LoginParamQuery loginParamQuery) {
        CsdnUser csdnUser = csdnUserService.findByUsername(loginParamQuery.getUsername());
        if (csdnUser == null) {
            throw new BusinessException("用户不存在");
        }
        return csdnUser;
    }
}
