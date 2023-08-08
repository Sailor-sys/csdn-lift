package com.xzl.csdn.remote.response;

import lombok.Data;

/**
 * @author gll
 * 2019/12/24 17:13
 */
@Data
public class LoginResponse {

    private String token;

    private UserInfoAreaVO area;


}
