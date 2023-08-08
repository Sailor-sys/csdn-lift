package com.xzl.csdn.enums;

import lombok.Getter;

/**
 * @author shiqh
 * @date 2023-07-24 15:34
 * @desc 故障类型
 **/
@Getter
public enum AlarmTypeEnum {

    KR(1000007,"困人次数"),
    TZ(1000150, "梯阻次数"),
    ZDM(1000010, "遮挡门次数"),
    YLW(1000095, "遗留物次数"),
    DJWP(1000050, "大件物品次数"),
    MQG(1000108,"煤气罐次数"),
    ZASJ(1000019,"治安事件次数"),
    XY(1000016,"吸烟次数"),

    ;

    private final Integer code;

    private final String name;

    AlarmTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(String code) {
        for (AlarmTypeEnum value : AlarmTypeEnum.values()) {
            if (String.valueOf(value.getCode()).equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
