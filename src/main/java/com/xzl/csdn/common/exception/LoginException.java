package com.xzl.csdn.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author：lianp
 * @description：
 * @date：9:41 2019/2/27
 */
public class LoginException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public LoginException(String msg) {
        super(msg);
    }

    public LoginException(String msg, Throwable t) {
        super(msg, t);
    }
}
