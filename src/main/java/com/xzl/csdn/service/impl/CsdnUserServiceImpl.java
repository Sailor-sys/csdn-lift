package com.xzl.csdn.service.impl;

import com.xzl.csdn.domain.entity.CsdnUser;
import com.xzl.csdn.remote.CsdnLiftRemoteApi;
import com.xzl.csdn.service.CsdnUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shiqh
 * @date 2023-08-01 14:07
 **/
@Service
public class CsdnUserServiceImpl implements CsdnUserService {

    @Autowired
    private CsdnLiftRemoteApi csdnLiftRemoteApi;

    @Override
    public String getUseUnitName(Integer userId) {
        return null;
    }

    @Override
    public CsdnUser findByUserId(Integer userId) {
        return csdnLiftRemoteApi.queryUser(null, userId).getData();
    }

    @Override
    public CsdnUser findByUsername(String username) {
        return csdnLiftRemoteApi.queryUser(username, null).getData();
    }
}
