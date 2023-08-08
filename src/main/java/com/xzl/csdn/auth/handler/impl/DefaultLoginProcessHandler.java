package com.xzl.csdn.auth.handler.impl;

import com.google.common.base.Strings;
import com.xzl.csdn.auth.model.LoginParamQuery;
import com.xzl.csdn.auth.model.LoginTypeEnum;
import com.xzl.csdn.auth.service.LoginService;
import com.xzl.csdn.common.exception.BusinessException;
import com.xzl.csdn.constant.Constants;
import com.xzl.csdn.domain.entity.CsdnUser;
import com.xzl.csdn.service.CsdnUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

/**
 * @author: liupu
 * @description: 用户名密码登录
 * @date: 2021/8/9
 */
@Component
public class DefaultLoginProcessHandler extends AbsLoginProcessor {

    @Autowired
    private CsdnUserService csdnUserService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public Integer loginType() {
        return LoginTypeEnum.DEFAULT.getCode();
    }

    @Override
    public void beforeCheck(LoginParamQuery loginParamQuery) {
        if (Strings.isNullOrEmpty(loginParamQuery.getPassword()) || Strings.isNullOrEmpty(loginParamQuery.getUsername())) {
            throw new BusinessException("用户名密码不能为空");
        }
        String loginErrorNum = jedisCluster.get(Constants.LOGINERRORNUM + loginParamQuery.getUsername());
        if (!StringUtils.isEmpty(loginErrorNum) && Integer.parseInt(loginErrorNum) >= Constants.LOGIN_ERROR_NUM_MAX) {
            long time = jedisCluster.ttl(Constants.LOGINERRORNUM + loginParamQuery.getUsername());
            throw new BusinessException("账号输入错误次数太多，请" + time + "秒后再尝试登录！");
        }

        imageCodeCheck(loginParamQuery);
    }

    @Override
    public void postCheck(LoginParamQuery loginParamQuery, CsdnUser csdnUser) {
        super.postCheck(loginParamQuery, csdnUser);
        //校验密码是否正确
        if (!loginParamQuery.getPassword().equals(csdnUser.getPassword())) {
            StringBuilder sb = new StringBuilder();
            setLoginErrorNum(csdnUser.getUsername());
            String loginErrorNum = jedisCluster.get(Constants.LOGINERRORNUM + csdnUser.getUsername());
            if (Constants.LOGIN_ERROR_NUM_MAX.intValue() == Integer.valueOf(loginErrorNum).intValue()) {
                long time = jedisCluster.ttl(Constants.LOGINERRORNUM + loginParamQuery.getUsername());
                sb.append("账号输入错误次数太多，请").append(time).append("秒后再尝试登录！");
            } else {
                sb.append("用户名或密码输入错误，登录失败!剩余登录次数：").append(Constants.LOGIN_ERROR_NUM_MAX - Integer.parseInt(loginErrorNum));
            }
            throw new BusinessException(sb.toString());
        }
        CsdnUser csdnUserRole = csdnUserService.findByUserId(csdnUser.getId());
        if (loginParamQuery.getPlatformType() == null) {
            if (csdnUserRole != null && "RESCUE_PLATFORM".equals(csdnUserRole.getRoleCode())) {
                throw new BusinessException("该账户无权限登录");
            }
        } else if (loginParamQuery.getPlatformType() == 1) {
            if (csdnUserRole != null && !"RESCUE_PLATFORM".equals(csdnUserRole.getRoleCode())) {
                throw new BusinessException("该账户无权限登录");
            }
        }
        //登录成功删除该用户登录失败的次数
        jedisCluster.del(Constants.LOGINERRORNUM + csdnUser.getUsername());
        loginService.deleteCacheImageCode(loginParamQuery.getImageCodeKey());
    }

    @Override
    protected CsdnUser loadBasic(LoginParamQuery loginParamQuery) {
        CsdnUser csdnUser = csdnUserService.findByUsername(loginParamQuery.getUsername());
        if (csdnUser == null) {
            StringBuilder sb = new StringBuilder();
            setLoginErrorNum(loginParamQuery.getUsername());
            String loginErrorNum = jedisCluster.get(Constants.LOGINERRORNUM + loginParamQuery.getUsername());
            sb.append("用户名或密码输入错误，登录失败!剩余登录次数：").append(Constants.LOGIN_ERROR_NUM_MAX - Integer.parseInt(loginErrorNum));
            throw new BusinessException(sb.toString());
        }
        return csdnUser;
    }


    private void setLoginErrorNum(String userName) {
        if (jedisCluster.exists(Constants.LOGINERRORNUM + userName)) {
            jedisCluster.incr(Constants.LOGINERRORNUM + userName);
            jedisCluster.expire(Constants.LOGINERRORNUM + userName, 60 * 10);
        } else {
            SetParams setParams = new SetParams();
            setParams.nx();
            setParams.ex(60 * 10);
            jedisCluster.set(Constants.LOGINERRORNUM + userName, "1", setParams);
        }
    }

    private void imageCodeCheck(LoginParamQuery loginParamQuery) {
        String sessionImageCode = loginService.getCodeFromCache(loginParamQuery.getImageCodeKey());
        //每次校验完验证码就删除imageCodekey
        loginService.deleteCacheImageCode(loginParamQuery.getImageCodeKey());
        if (loginParamQuery.getImageCode() == null) {
            throw new BusinessException("验证码不能为空");
        }
        if (sessionImageCode == null) {
            throw new BusinessException("验证码过期，请重新获取验证码");
        }
        if (!sessionImageCode.equals(loginParamQuery.getImageCode())) {
            throw new BusinessException("验证码错误");
        }
    }

}
