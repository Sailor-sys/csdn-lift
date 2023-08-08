package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-20 17:46
 * @desc 电梯数量
 **/
@ApiModel("基本电梯数量")
@Data
public class CsdnLiftNumVO {

    @ApiModelProperty("电梯总量")
    private Integer totalCount;

    @ApiModelProperty("累计覆盖人次")
    private Integer coverageCount;

}
