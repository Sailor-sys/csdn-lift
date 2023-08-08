package com.xzl.csdn.common.http.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author shiqh
 * @date 2023-06-30 16:40
 * @desc 电梯运行统计数据
 **/
@Data
public class CsdnWlwLiftRunningStatistics {

    /**
     * 电梯注册代码
     */
    private String registerCode;

    /**
     * 开门次数
     */
    private Integer openDoorCount;

    /**
     *
     */
    private Integer ropeBendingTimes;

    /**
     * 运行距离，单位米
     */
    private BigDecimal runDistance;

    /**
     * 运行时间,单位秒
     */
    private BigDecimal runDuration;

    /**
     * 运行次数
     */
    private Integer runTimes;

}
