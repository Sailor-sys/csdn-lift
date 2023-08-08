package com.xzl.csdn.domain.query;

import lombok.Data;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-08-01 14:48
 * @desc 告警类型数量
 **/
@Data
public class AlarmTypeCountParam {

    /**
     * 区域编号
     */
    private String townCode;

    /**
     * 注册代码
     */
    private List<String> registerCodeList;

    /**
     * 开始时间
     */
    private long beginTime;

    /**
     * 结束时间
     */
    private long endTime;

}
