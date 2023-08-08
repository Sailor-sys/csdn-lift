package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-21 17:01
 * @desc 故障分析表单对象
 **/
@Data
@ApiModel("故障分析表单对象")
public class MalfunctionFromVO {

    @ApiModelProperty("故障设备短码")
    private String deviceNo;

    @ApiModelProperty("场所名称")
    private String locationName;

    @ApiModelProperty("场所类型")
    private String locationType;

    @ApiModelProperty("电梯编号")
    private String innerNo;

    @ApiModelProperty("维保单位")
    private String maintenanceUnitName;

    @ApiModelProperty("使用名称")
    private String useUnitName;

    @ApiModelProperty("告警次数/运行里程")
    private String alarmCount;

}
