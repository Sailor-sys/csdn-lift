package com.xzl.csdn.common.exception;

import com.xzl.csdn.config.proxy.DataCenterTokenProxy;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * @author gll
 * 2019/8/21 13:23
 */
@Slf4j
public class DataCenterErrorDecoder implements ErrorDecoder {

    @Setter
    DataCenterTokenProxy dataCenterTokenProxy;
    @Override
    public Exception decode(String methodKey, Response response) {
        if(response.status()== HttpStatus.UNAUTHORIZED.value()){
            try{
                log.error("occur unauthorized error");
                dataCenterTokenProxy.resetToken();
            }catch (Exception e){
                log.error("reset token occur exception",e);
            }
        }
        return FeignException.errorStatus(methodKey, response);
    }
}
