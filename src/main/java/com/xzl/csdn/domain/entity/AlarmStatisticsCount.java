package com.xzl.csdn.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * AlarmStatisticsCountDTO类
 * 各故障类型总数统计
 *
 * @author : Troy Zhou
 * @date : 2022-03-17 15:10
 **/
@ApiModel("故障分析")
@Data
public class AlarmStatisticsCount {
    /**
     * 困人故障数量
     */
    @ApiModelProperty("困人故障数量")
    private Integer sleepyCount;
    /**
     * 开关门数量
     */
    @ApiModelProperty("开关门数量")
    private Integer openCloseCount;
    /**
     * 超速运行故障
     */
    @ApiModelProperty("超速运行故障")
    private Integer overSpeedCount;
    /**
     * 急停故障
     */
    @ApiModelProperty("急停故障")
    private Integer emergencyStopCount;
    /**
     * 开门运行故障
     */
    @ApiModelProperty("开门运行故障")
    private Integer openCount;
    /**
     * 其他故障
     */
    @ApiModelProperty("其他故障")
    private Integer otherCount;

    /**
     * 计算总和
     */
    public Integer getAlarmSum() {
        return this.getSleepyCount() + this.getOpenCloseCount() + this.overSpeedCount + this.getEmergencyStopCount() + this.getOpenCount()
                + this.otherCount;
    }

    /**
     * 计算各类型占比
     */
    public void calculateRate() {
        Integer alarmSum = getAlarmSum();
        Integer sleepyRate = Integer.parseInt(String.valueOf(new BigDecimal(this.getSleepyCount())
                .divide(new BigDecimal(alarmSum), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100))));
        this.setSleepyCount(sleepyRate);

        Integer openCloseRate = Integer.parseInt(String.valueOf(new BigDecimal(this.getOpenCloseCount())
                .divide(new BigDecimal(alarmSum), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100))));
        this.setOpenCloseCount(openCloseRate);

        Integer overSpeedRate = Integer.parseInt(String.valueOf(new BigDecimal(this.getOverSpeedCount())
                .divide(new BigDecimal(alarmSum), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100))));
        this.setOverSpeedCount(overSpeedRate);

        Integer emergencyStopRate = Integer.parseInt(String.valueOf(new BigDecimal(this.getEmergencyStopCount())
                .divide(new BigDecimal(alarmSum), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100))));
        this.setEmergencyStopCount(emergencyStopRate);

        Integer openRate = Integer.parseInt(String.valueOf(new BigDecimal(this.getOpenCount())
                .divide(new BigDecimal(alarmSum), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100))));
        this.setOpenCount(openRate);

        Integer otherRate = Integer.parseInt(String.valueOf(new BigDecimal(this.getOtherCount())
                .divide(new BigDecimal(alarmSum), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100))));
        this.setOtherCount(otherRate);
    }
}
