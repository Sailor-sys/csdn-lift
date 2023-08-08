package com.xzl.csdn.common.http;

import lombok.Data;

@Data
public class RequestHeader {
    public RequestHeader(){

    }

    public RequestHeader(String key, String value){
        this.key = key;
        this.value = value;
    }

    String key;

    String value;
}
