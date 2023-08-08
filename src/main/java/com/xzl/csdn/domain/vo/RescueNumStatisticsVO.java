package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-19 11:31
 **/
@Data
@ApiModel("应急救援响应对象")
public class RescueNumStatisticsVO {

    @ApiModelProperty("本月困人")
    private Integer monthTrappedPeople;

    @ApiModelProperty("本年困人")
    private Integer yearTrappedPeople;

    @ApiModelProperty("本月救人数量")
    private Integer monthRescuePeople;

    @ApiModelProperty("本年救人数量")
    private Integer yearRescuePeople;

    @ApiModelProperty("平均救援用时")
    private String avgRescueTime;

    @ApiModelProperty("平均传统救援用时")
    private String avgTraditionRescueTime;

}
