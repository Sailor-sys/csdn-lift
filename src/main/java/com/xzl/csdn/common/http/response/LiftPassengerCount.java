package com.xzl.csdn.common.http.response;

import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-20 16:26
 * @desc 乘梯人数统计
 **/
@Data
public class LiftPassengerCount {

    private String registerCode;

    private String count;
}
