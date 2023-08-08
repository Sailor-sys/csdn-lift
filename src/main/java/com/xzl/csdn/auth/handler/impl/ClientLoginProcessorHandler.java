package com.xzl.csdn.auth.handler.impl;

import com.google.common.base.Strings;
import com.xzl.csdn.auth.model.LoginParamQuery;
import com.xzl.csdn.auth.model.LoginTypeEnum;
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
 * @description: 其他服务登陆模式
 * @date: 2021/8/9
 */
@Component
public class ClientLoginProcessorHandler extends AbsLoginProcessor {
    @Autowired
    private CsdnUserService csdnUserService;
    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public Integer loginType() {
        return LoginTypeEnum.CLIENT_MODE.getCode();
    }

    @Override
    public void beforeCheck(LoginParamQuery loginParamQuery) {
        if (Strings.isNullOrEmpty(loginParamQuery.getPassword()) || Strings.isNullOrEmpty(loginParamQuery.getUsername())) {
            throw new BusinessException("用户名密码不能为空");
        }
        String loginErrorNum = jedisCluster.get(Constants.LOGINERRORNUM + loginParamQuery.getUsername());
        if (!StringUtils.isEmpty(loginErrorNum) && Integer.parseInt(loginErrorNum) >= Constants.LOGIN_ERROR_NUM_MAX) {
            long time = jedisCluster.ttl(Constants.LOGINERRORNUM + loginParamQuery.getUsername());
            throw new BusinessException("用户名密码不能为空");
        }
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

    @Override
    public void postCheck(LoginParamQuery loginParamQuery, CsdnUser csdnUser) {
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
        //登录成功删除该用户登录失败的次数
        jedisCluster.del(Constants.LOGINERRORNUM + csdnUser.getUsername());
        super.postCheck(loginParamQuery, csdnUser);
    }
}
