package com.xzl.csdn.common.exception;

import com.xzl.csdn.support.ApiResult;
import com.xzl.csdn.support.ApiStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.DataTruncation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author：lianp
 * 异常集中处理类
 * @date：11:06 2018/4/12
 */
@SuppressWarnings("unused")
@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice {

    /**
     * 处理参数验证错误
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleBadRequest(MethodArgumentNotValidException exception) {
        log.error("handle bad request",exception);
        return handleBindingError(exception.getBindingResult());
    }

    /**
     * 处理参数验证错误
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Object handleBadRequest(BindException exception) {
        log.error("handle bad request",exception);
        return handleBindingError(exception.getBindingResult());
    }

    ApiResult handleBindingError(BindingResult bindingResult) {
        ApiResult result = new ApiResult();
        StringBuilder builder = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError e = (FieldError) error;
                builder.append(e.getField())
                        .append(' ')
                        .append(error.getDefaultMessage())
                        .append('\n');
            } else {
                builder.append(error.getDefaultMessage()).append('\n');
            }
        }
        result.setStatus(ApiStatus.PARAM_DATA_ERROR);
        result.setMessage(builder.toString());
        return result;
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public ApiResult onSqlException(BadSqlGrammarException e, HttpServletRequest request) {

        log.error("on sql exception",e);

        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));

        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(ApiStatus.INTERNAL_SERVER_ERROR);
        apiResult.setMessage("sql error");

        return apiResult;
    }

    @ExceptionHandler(DataTruncation.class)
    @ResponseBody
    public ApiResult onDataTruncation(DataTruncation e){
        log.error("dataTruncation error",e);
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(ApiStatus.INTERNAL_SERVER_ERROR);
        apiResult.setMessage("sql error");
        return apiResult;
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult onViolationFailed(ConstraintViolationException e){
        log.error("on violation failed ",e);
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(ApiStatus.PARAM_DATA_ERROR);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        if(!CollectionUtils.isEmpty(violations)){
            ConstraintViolation<?> val = violations.iterator().next();
            apiResult.setMessage(val.getMessage());
        }
        return apiResult;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ApiResult onBusinessFailed(BusinessException e) {
        log.error("on business failed",e);

        ApiResult apiResult = new ApiResult();
        if(Objects.isNull(e.getErrorCode())){
            apiResult.setStatus(ApiStatus.INTERNAL_SERVER_ERROR);
        }else{
            apiResult.setStatus(e.getErrorCode());
        }
        apiResult.setMessage(e.getMessage());
        apiResult.setData(null);

        return apiResult;
    }

    @ExceptionHandler(AuthFailException.class)
    @ResponseBody
    public Map<String,String> onAuthFailException(AuthFailException e) {
        log.error("on cs Auth failed",e);
        Map<String,String> result = new HashMap<>();
        result.put("code",e.getCode());
        result.put("msg",e.getMsg());
        return result;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ApiResult onExceptionFailed(RuntimeException e) {
        log.error("on Exception ",e);

        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(ApiStatus.INTERNAL_SERVER_ERROR);
        apiResult.setData(null);

        if(e instanceof UnAuthenticationException){
        	throw new UnAuthenticationException();
		}
        return apiResult;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ApiResult onIllegalArgumentExceptionFailed(IllegalArgumentException e) {
        log.error("on IllegalArgumentException ", e);
        ApiResult apiResult = new ApiResult();
        apiResult.setData(null);
        apiResult.setStatus(ApiStatus.PARAM_DATA_ERROR);
        apiResult.setMessage(e.getMessage());
        return apiResult;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ApiResult onHttpMessageNotReadableExceptionFailed(HttpMessageNotReadableException e) {
        log.error("on MismatchedInputException ", e);
        ApiResult apiResult = new ApiResult();
        apiResult.setData(null);
        apiResult.setStatus(ApiStatus.PARAM_DATA_FORMAT_ERROR);
        apiResult.setMessage(ApiStatus.PARAM_DATA_FORMAT_ERROR.getMessage());
        return apiResult;
    }
}
