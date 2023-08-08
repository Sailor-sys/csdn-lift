package com.xzl.csdn.service;

import com.xzl.csdn.domain.entity.CsdnUser;

/**
 * @author shiqh
 * @date 2023-08-01 14:07
 **/
public interface CsdnUserService {

    String getUseUnitName(Integer userId);

    CsdnUser findByUserId(Integer userId);

    CsdnUser findByUsername(String username);
}
