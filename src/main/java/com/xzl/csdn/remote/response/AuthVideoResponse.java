package com.xzl.csdn.remote.response;

import lombok.Data;

@Data
public class AuthVideoResponse {

    private Integer expiration;

    private String url;

    private String urlSec;

}
