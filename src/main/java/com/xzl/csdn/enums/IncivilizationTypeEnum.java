package com.xzl.csdn.enums;

import lombok.Getter;

/**
 * @author shiqh
 * @date 2023-07-26 16:42
 * @desc 不文明类型
 **/
@Getter
public enum IncivilizationTypeEnum {

    ZDM(1000010, "遮挡门次数"),
    YLW(1000095, "遗留物次数"),
    DJWP(1000050, "大件物品次数"),
    MQG(1000108,"煤气罐次数"),
    ZASJ(1000019,"治安事件次数"),
    XY(1000016,"吸烟次数")

            ;

    private Integer code;

    private String name;

    IncivilizationTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

}
