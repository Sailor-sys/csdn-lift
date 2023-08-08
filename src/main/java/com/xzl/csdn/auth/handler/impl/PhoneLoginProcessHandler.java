package com.xzl.csdn.auth.handler.impl;

import com.google.common.base.Strings;
import com.xzl.csdn.auth.model.LoginParamQuery;
import com.xzl.csdn.auth.model.LoginTypeEnum;
import com.xzl.csdn.auth.service.LoginService;
import com.xzl.csdn.common.exception.BusinessException;
import com.xzl.csdn.constant.Constants;
import com.xzl.csdn.domain.entity.CsdnUser;
import com.xzl.csdn.service.CsdnUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.Optional;

/**
 * @author: liupu
 * @description: 手机验证码登录
 * @date: 2021/8/9
 */
@Component
public class PhoneLoginProcessHandler extends AbsLoginProcessor{

    @Autowired
    private LoginService loginService;
	@Autowired
	private CsdnUserService csdnUserService;
	@Autowired
	private JedisCluster jedisCluster;

	@Override
    public Integer loginType() {
        return LoginTypeEnum.MOBILE_PHONE.getCode();
    }

    @Override
    public void beforeCheck(LoginParamQuery loginParamQuery) {
        if(Strings.isNullOrEmpty(loginParamQuery.getUsername())){
            //手机号码不为空验证
            throw new BusinessException("手机号码不能为空");
        }
        if(Strings.isNullOrEmpty(loginParamQuery.getPassword())){
            //手机验证码验证不能为空
            throw new BusinessException("手机验证码不能为空");
        }

        String smsCode = loginService.getCodeFromCache(loginParamQuery.getUsername());
        if(!loginParamQuery.getPassword().equals(smsCode)){
            throw new BusinessException("验证码校验失败");
        }
    }

    @Override
    public void postCheck(LoginParamQuery loginParamQuery, CsdnUser csdnUser) {
        super.postCheck(loginParamQuery, csdnUser);
        //删除验证码
        loginService.deleteCacheImageCode(loginParamQuery.getUsername());
		//登录成功删除该用户登录失败的次数
		jedisCluster.del(Constants.LOGINERRORNUM + csdnUser.getUsername());
    }

    @Override
    protected CsdnUser loadBasic(LoginParamQuery loginParamQuery) {
        return null;
    }
}
