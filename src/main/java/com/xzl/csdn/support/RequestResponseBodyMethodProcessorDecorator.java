package com.xzl.csdn.support;

import lombok.Getter;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author：lianp
 * @date：11:06 2018/4/12
 */
public class RequestResponseBodyMethodProcessorDecorator implements HandlerMethodReturnValueHandler {

    HandlerMethodReturnValueHandler delegate;

    public RequestResponseBodyMethodProcessorDecorator(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (!(returnValue instanceof ApiResult)) {
            RestApi restApi = returnType.getMethodAnnotation(RestApi.class);
            if (restApi == null) {
                restApi = returnType.getDeclaringClass().getAnnotation(RestApi.class);
            }
            if (restApi != null) {
                ApiResult result = new ApiResult();
                if (restApi.wrapResult()) {
                    result.setData(new Result(returnValue));
                } else {
                    result.setData(returnValue);
                }
                returnValue = result;
            }
        }
        delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }


    static class Result {
        public Result(Object result) {
            this.result = result;
        }

        @Getter
        Object result;
    }
}
