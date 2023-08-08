package com.xzl.csdn.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author gll
 * 2019/8/21 13:17
 */
@Import(DataCenterGlobalConfig.class)
public class DataCenterFeignTokenConfig {


    @Bean
    public RequestInterceptor getRequestInterceptor(){
        return requestTemplate -> {
            //token请求，不需要设置头部信息
        };
    }

}
