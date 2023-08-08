package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-24 17:59
 * @desc 告警节点时间
 **/
@Data
@ApiModel("告警节点时间")
public class AlarmNoteTimeVO {

    @ApiModelProperty("告警编号")
    private String errorNo;

    @ApiModelProperty("通知时间")
    private String notifyTime;

    @ApiModelProperty("接收时间")
    private String receivedTime;

    @ApiModelProperty("派遣时间")
    private String sendTime;

    @ApiModelProperty("到达时间")
    private String arriveTime;

    @ApiModelProperty("救援时间")
    private String rescueEndTime;

    @ApiModelProperty("维修时间")
    private String repairEndTime;

    @ApiModelProperty("关闭时间")
    private String endTime;

}
