package com.xzl.csdn.enums;

import lombok.Getter;

/**
 * @author shiqh
 * @date 2023-07-20 10:45
 * @desc 故障类型枚举
 **/
public enum ErrorTypeEnum {

    /**
     * 困人告警
     */
    ALARM_TYPE_KR(1000007,"困人"),
    ALARM_TYPE_TT(1000005,"停梯"),
    ALARM_TYPE_FW(9000002,"复位"),
    ALARM_TYPE_JT(1000040,"急停"),
    ALARM_TYPE_ZD(1000019,"震动"),
    ALARM_TYPE_CSJDM(1000010,"长时间挡门"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String name;

    ErrorTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }


}
