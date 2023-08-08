package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-19 17:10
 **/
@ApiModel("故障分析")
@Data
public class MalfunctionRateVO {

    @ApiModelProperty("困人")
    private String trappedRate = "0";

    @ApiModelProperty("停梯")
    private String stopRate = "0";

    @ApiModelProperty("复位")
    private String resetRate = "0";

    @ApiModelProperty("急停")
    private String exigencyRate = "0";

    @ApiModelProperty("震动")
    private String shakeRate = "0";

    @ApiModelProperty("长时间挡门")
    private String shieldRate = "0";

}
