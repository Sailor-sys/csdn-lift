package com.xzl.csdn.remote.request;

import lombok.Data;

/**
 * @author gll
 * 2019/12/24 17:11
 */
@Data
public class LoginParam {
    private String username;
    private String password;
    private Integer loginType=3;
}
