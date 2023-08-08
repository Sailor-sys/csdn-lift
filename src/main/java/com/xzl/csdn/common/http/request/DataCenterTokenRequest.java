package com.xzl.csdn.common.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gll
 * 2019/8/19 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataCenterTokenRequest {

    private String clientName;

    private String clientPassword;
}
