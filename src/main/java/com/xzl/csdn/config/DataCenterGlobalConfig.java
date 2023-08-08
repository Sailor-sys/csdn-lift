package com.xzl.csdn.config;

import feign.Client;
import feign.Request;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.context.annotation.Bean;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author gll
 * 2019/8/21 13:18
 */
public class DataCenterGlobalConfig {
    @Bean
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory) throws NoSuchAlgorithmException, KeyManagementException {
        return DefaultHttpsClient.feignClient(cachingFactory,clientFactory);
    }



    @Bean
    public Request.Options options() {
        return new Request.Options(10 * 1000, 2 * 60 * 1000);
    }
}
