package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-24 13:40
 * @desc 电梯点位
 **/
@Data
@ApiModel("电梯点位")
public class CsdnLiftPositionVO {

    @ApiModelProperty("电梯注册代码")
    private String registerCode;

    @ApiModelProperty("告警编号")
    private String errorNo;

    @ApiModelProperty("经度")
    private String lon;

    @ApiModelProperty("维度")
    private String lat;

}
