package com.xzl.csdn.common.exception;

import lombok.Data;

/**
 * @author gll
 * 2019/7/3 17:39
 */
@Data
public class AuthFailException extends RuntimeException{
    String code = "1";

    String msg = "成功";

    public AuthFailException(String code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
