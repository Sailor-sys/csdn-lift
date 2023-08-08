package com.xzl.csdn.common.http.response;

import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-25 17:17
 * 告警类型响应对象
 **/
@Data
public class RecentlyResponse {

    private String registerCode;
    private Integer count;
    private String alarmType;
}
