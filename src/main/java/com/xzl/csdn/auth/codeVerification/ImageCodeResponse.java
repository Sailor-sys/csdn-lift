package com.xzl.csdn.auth.codeVerification;

import lombok.Data;

/**
 * @author: liupu
 * @description:
 * @date: 2021/8/9
 */
@Data
public class ImageCodeResponse {

    private String imageCodeKey;

    private ImageCode imageCode;


}
