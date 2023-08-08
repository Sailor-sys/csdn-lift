package com.xzl.csdn.config;

import com.xzl.csdn.remote.LiftAgentRemoteApi;
import com.xzl.csdn.remote.request.LoginParam;
import com.xzl.zq.remote.common.token.RedisTokenCacheService;
import feign.Client;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.context.annotation.Bean;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author gll
 * 2019/12/11 17:13
 */
public class DefaultHttpsConfig {

    @Autowired
    RedisTokenCacheService redisTokenCacheService;

    @Bean
    public Client feignClient() throws NoSuchAlgorithmException, KeyManagementException {
        return DefaultHttpsClient.feignClient();
    }

    @Bean
    public RequestInterceptor getRequestInterceptor() {
        return requestTemplate -> {
            String token = redisTokenCacheService.get("LIFT_AGENT_TOKEN");
//            String token = null;
            //自带header token的不会覆盖
            if (token != null && !requestTemplate.headers().containsKey("Authorization")) {
                /** 设置请求头信息 **/
                requestTemplate.header("Authorization", token);
            }
        };
    }
}
