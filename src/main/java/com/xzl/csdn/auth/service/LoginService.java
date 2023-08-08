package com.xzl.csdn.auth.service;


import com.xzl.csdn.auth.codeVerification.ImageCodeResponse;
import com.xzl.csdn.auth.model.CsdnUserVO;
import com.xzl.csdn.auth.model.LoginParamQuery;

public interface LoginService {
    /**
     * 删除缓存验证码
     */
    void deleteCacheImageCode(String imageCodeKey);

    /**
     * 获取图形验证码
     */
    ImageCodeResponse getImageCode();

    /**
     * 登陆
     */
    CsdnUserVO login(LoginParamQuery loginParamQuery);

	/**HomeController
	 * 根据token获取用户信息
	 */
	CsdnUserVO getUserByToken(String token);

	/**
     * 根据缓存验证码获取imageCode
     */
    String getCodeFromCache(String codeKey);
}
