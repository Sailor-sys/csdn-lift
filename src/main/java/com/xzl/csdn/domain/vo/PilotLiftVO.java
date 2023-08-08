package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-20 13:47
 * @desc 试点电梯
 **/
@Data
public class PilotLiftVO {

    @ApiModelProperty("年累计运行里程")
    private Integer yearRunKm;

    @ApiModelProperty("年累计覆盖人次")
    private Integer yearUsePeopleNum;

    @ApiModelProperty("年累计不安全文明乘梯")
    private Integer yearNoCivilizedLadder;

}
