package com.xzl.csdn.config.proxy;

import com.alibaba.fastjson.JSON;
import com.xzl.csdn.common.http.request.DataCenterTokenRequest;
import com.xzl.csdn.common.http.response.DataCenterTokenResponse;
import com.xzl.csdn.remote.DataCenterTokenRemoteApi;
import com.xzl.csdn.service.AbsTokenService;
import com.xzl.zq.csdn.response.CsdnBaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author gll
 * 2019/8/19 15:07
 * 数据中心token代理类
 */
@Service
@Slf4j
public class DataCenterTokenProxy extends AbsTokenService {


    @Value("${csdn.client.id}")
    private String clientName;
    @Value("${csdn.client.secret}")
    private String clientPassword;

    Lock lock = new ReentrantLock();

    private static String TOKEN_KEY_PREFIX = "dataCenterTokenKey_";

    @Autowired
    private DataCenterTokenRemoteApi dataCenterTokenRemoteApi;

    public void resetToken() {
        super.resetTokenTemplate();
    }


    @Override
    protected String getTokenFromRemote() {
        return generatorToken();
    }

    @Override
    protected String getTokenKeyPrefix() {
        return TOKEN_KEY_PREFIX;
    }

    @Override
    protected Lock getLock() {
        return lock;
    }

    public String getToken() {
        return super.getTokenTemplate();
    }

    private String generatorToken() {
        DataCenterTokenRequest request = new DataCenterTokenRequest(clientName, clientPassword);
        CsdnBaseResponse<DataCenterTokenResponse> tokenResponse = dataCenterTokenRemoteApi.getToken(request);
        log.info("datacenter token response,json={}", JSON.toJSONString(tokenResponse));
        if (tokenResponse != null && tokenResponse.getData() != null) {
            return tokenResponse.getData().getToken();
        }
        return null;
    }
}
