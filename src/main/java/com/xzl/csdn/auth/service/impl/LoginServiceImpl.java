package com.xzl.csdn.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.xzl.csdn.auth.codeVerification.ImageCode;
import com.xzl.csdn.auth.codeVerification.ImageCodeResponse;
import com.xzl.csdn.auth.handler.LoginProcessHandler;
import com.xzl.csdn.auth.model.CsdnUserVO;
import com.xzl.csdn.auth.model.JwtUser;
import com.xzl.csdn.auth.model.LoginParamQuery;
import com.xzl.csdn.auth.model.LoginTypeEnum;
import com.xzl.csdn.auth.service.JwtTokenService;
import com.xzl.csdn.auth.service.LoginService;
import com.xzl.csdn.auth.utils.SessionUtil;
import com.xzl.csdn.common.exception.BusinessException;
import com.xzl.csdn.domain.entity.CsdnUser;
import com.xzl.csdn.service.CsdnUserService;
import com.xzl.csdn.utils.Sha256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author XZL
 */
@Component
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Value("${spring.application.name:empty}")
    private String application;
    @Value("${spring.profiles.active:empty}")
    private String profile;

    private Map<Integer, LoginProcessHandler> loginHandlerMap;
    @Autowired
    List<LoginProcessHandler> loginHandlerList;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private CsdnUserService csdnUserService;

    /**
     * 超时时间1个小时
     */
    private static final Integer IMAGE_CODE_EXPIRE = 60 * 60;

    @PostConstruct
    public void init() {
        loginHandlerMap = loginHandlerList.stream().collect(Collectors.toMap(LoginProcessHandler::loginType, Function.identity()));
    }


    @Override
    public void deleteCacheImageCode(String imageCodeKey) {
        //删除缓存验证码
        jedisCluster.del(getSessionKey(imageCodeKey));

        //登陆成功删除短信间隔时间
        jedisCluster.del(getSmsIntervalKey(imageCodeKey));
    }

    @Override
    public ImageCodeResponse getImageCode() {
        ImageCode imageCode = ImageCode.createImageCode();
        ImageCodeResponse response = new ImageCodeResponse();
        response.setImageCode(imageCode);
        response.setImageCodeKey(UUID.randomUUID().toString());
        cacheCode(response.getImageCodeKey(), imageCode.getCode(), IMAGE_CODE_EXPIRE);
        return response;
    }

    private void cacheCode(String key, String code, Integer expire) {
        jedisCluster.setex(getSessionKey(key), expire, code);
    }

    private String getSessionKey(String sessionId) {
        return getKeyPrefix() + sessionId + "_code";
    }

    private String getKeyPrefix() {
        return application + "_" + profile + "_";
    }

    private CsdnUser checkAndLoad(LoginParamQuery loginParamQuery) {
        //获取处理器
        LoginProcessHandler checkHandler = Optional.ofNullable(
                loginHandlerMap.get(loginParamQuery.getLoginType())).orElseThrow(() -> new BusinessException("登陆类型错误"));

        //前置校验
        checkHandler.beforeCheck(loginParamQuery);

        //加载用户信息
        CsdnUser csdnUser = checkHandler.load(loginParamQuery);
        if (log.isDebugEnabled()) {
            log.debug("load userInf={}", JSON.toJSONString(csdnUser));
        }
        //后置校验
        checkHandler.postCheck(loginParamQuery, csdnUser);

        return csdnUser;
    }


    @Override
    public CsdnUserVO login(LoginParamQuery loginParamQuery) {
        loginParamQuery.setLoginType(LoginTypeEnum.DEFAULT.getCode());

        loginParamQuery.setPassword(Sha256Util.string2SHA256StrJava(loginParamQuery.getPassword()));
        CsdnUser csdnUser = checkAndLoad(loginParamQuery);

        JwtUser jwtUser = buildJwtUser(csdnUser, loginParamQuery);
        String token = jwtTokenService.generateToken(jwtUser);

        String username = jwtUser.getUsername();
        jwtTokenService.cacheNewToken(token, username, jwtUser);
        return buildUserModel(csdnUser, token);
    }

    @Override
    public CsdnUserVO getUserByToken(String token) {
        CsdnUser csdnUser = csdnUserService.findByUserId(SessionUtil.getUserId(null));
        return buildUserModel(csdnUser, token);
    }


    @Override
    public String getCodeFromCache(String imageCodeKey) {
        //获取验证码
        return jedisCluster.get(getSessionKey(imageCodeKey));
    }


    private String getSmsIntervalKey(String phoneNo) {
        return getKeyPrefix() + "_sms_interval_" + phoneNo;
    }


    private static Random random;

    static {
        try {
            random = SecureRandom.getInstance("NativePRNGNonBlocking");
        } catch (NoSuchAlgorithmException var1) {
            random = new Random();
        }

    }

    private CsdnUserVO buildUserModel(CsdnUser csdnUser, String token) {
        CsdnUserVO userModel = new CsdnUserVO();
        userModel.setPhoneNo(csdnUser.getPhone());
        userModel.setUsername(csdnUser.getUsername());
        userModel.setNickName(csdnUser.getNickname());
        userModel.setUserId(csdnUser.getId().toString());
        userModel.setModifyPwdFlag(csdnUser.getLoginTag());
        userModel.setToken(token);
        userModel.setLoginTag(csdnUser.getLoginTag());
        userModel.setUnitId(csdnUser.getUnitId());
        userModel.setActualPureColor(csdnUser.getActualPureColor());
        userModel.setPlaybackPureColor(csdnUser.getPlaybackPureColor());

        boolean userIsAdmin = false;
        userModel.setUserIsAdmin(userIsAdmin);
        return userModel;
    }

    private JwtUser buildJwtUser(CsdnUser csdnUser, LoginParamQuery loginParamQuery) {
        JwtUser jwtUser = new JwtUser();
        jwtUser.setId(csdnUser.getId());
        jwtUser.setUuid(UUID.randomUUID().toString());
        jwtUser.setUsername(csdnUser.getUsername());
        jwtUser.setNickName(csdnUser.getNickname());
        jwtUser.setMobilePhone(csdnUser.getPhone());
        jwtUser.setLoginType(loginParamQuery.getLoginType());
        jwtUser.setUnitId(csdnUser.getUnitId());
        return jwtUser;
    }

}
