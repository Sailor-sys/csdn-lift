package com.xzl.csdn.common.http.response;

import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-21 09:21
 * @desc 云梯token对象
 **/
@Data
public class TokenData {

    private String accessToken;


    private long expiresTime;

}
