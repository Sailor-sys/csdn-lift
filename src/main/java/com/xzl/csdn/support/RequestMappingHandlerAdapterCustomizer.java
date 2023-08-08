package com.xzl.csdn.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：lianp
 * @date：11:06 2018/4/12
 */
@Component
public class RequestMappingHandlerAdapterCustomizer {

    @Autowired
    RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    void configure(){
        List<HandlerMethodReturnValueHandler> returnValueHandlers= requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlersToUse=new ArrayList<>();
        for (HandlerMethodReturnValueHandler returnValueHandler : returnValueHandlers) {
            if(returnValueHandler instanceof RequestResponseBodyMethodProcessor){
                returnValueHandler=new RequestResponseBodyMethodProcessorDecorator(returnValueHandler);
            }
            handlersToUse.add(returnValueHandler);
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(handlersToUse);
    }
}
