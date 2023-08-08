package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author shiqh
 * @date 2023-07-24 17:49
 * @desc 电梯信息
 **/
@Data
@ApiModel("电梯信息")
public class CsdnLiftInfoVO {

    @ApiModelProperty("注册代码")
    private String registerCode;

    @ApiModelProperty("场所名称")
    private String locationName;

    @ApiModelProperty("设备编号")
    private String deviceNumber;

    @ApiModelProperty("内部编号")
    private String liftName;

    @ApiModelProperty("电梯使用地址")
    private String detailAddress;

    @ApiModelProperty("维保单位名称")
    private String maintainEnterName;

    @ApiModelProperty("使用单位")
    private String useUnitName;

    @ApiModelProperty("场所类型")
    private String locationType;

    /**
     * 街道编号
     */
    private String streetCode;

    /**
     * 制造时间
     */
    private Date madeTime;


}
