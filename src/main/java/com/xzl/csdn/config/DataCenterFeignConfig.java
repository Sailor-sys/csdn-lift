package com.xzl.csdn.config;

import com.xzl.csdn.common.exception.DataCenterErrorDecoder;
import com.xzl.csdn.config.proxy.DataCenterTokenProxy;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author gll
 * 2019/8/21 12:04
 */
@Import(DataCenterGlobalConfig.class)
public class DataCenterFeignConfig {


    @Autowired
    private DataCenterTokenProxy dataCenterTokenProxy;

    @Bean
    public RequestInterceptor getRequestInterceptor() {
        return requestTemplate -> {
            /** 设置请求头信息 **/
            String token = dataCenterTokenProxy.getToken();
            requestTemplate.header("token", token);
        };
    }

    @Bean
    public ErrorDecoder errorDecoder(DataCenterTokenProxy tokenProxy){
        DataCenterErrorDecoder errorDecoder = new DataCenterErrorDecoder();
        errorDecoder.setDataCenterTokenProxy(tokenProxy);
        return errorDecoder;
    }

    @Bean
    public Logger.Level loggerLevel(){
        return  Logger.Level.FULL;
    }
}
