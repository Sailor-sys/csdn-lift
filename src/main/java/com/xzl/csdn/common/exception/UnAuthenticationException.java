package com.xzl.csdn.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gll
 * 2019/7/3 17:39
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED,reason = "未授权")
public class UnAuthenticationException extends RuntimeException{

}
