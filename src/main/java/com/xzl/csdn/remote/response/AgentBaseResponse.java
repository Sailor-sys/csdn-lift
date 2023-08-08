package com.xzl.csdn.remote.response;

import lombok.Data;

/**
 * @author gll
 * 2019/12/24 17:13
 */
@Data
public class AgentBaseResponse<T> {

    private Integer code;

    private String message;

    private T data;
}
