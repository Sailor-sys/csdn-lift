package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-24 17:47
 * @desc 报警信息
 **/
@Data
@ApiModel("报警信息")
public class CsdnAlarmInfoVO {

    @ApiModelProperty("报警人名称")
    private String name;

    @ApiModelProperty("报警人手机号")
    private String phone;

    @ApiModelProperty("报警时间")
    private String time;

    @ApiModelProperty("报警来源")
    private String source;

    @ApiModelProperty("报警标签")
    private String remake;

    @ApiModelProperty("注册代码")
    private String registerCode;

}
