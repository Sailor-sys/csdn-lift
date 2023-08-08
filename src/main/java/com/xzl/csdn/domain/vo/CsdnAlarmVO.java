package com.xzl.csdn.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 告警信息
 */
@Data
public class CsdnAlarmVO {

    @ApiModelProperty("注册代码")
    private String registerCode;

    @ApiModelProperty("告警类型")
    private String errorTypeName;

    @ApiModelProperty("告警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date recordeTime;

    @ApiModelProperty("省-市-区-街道")
    private String regiName;

    @ApiModelProperty("场所名称")
    private String locationName;

    @ApiModelProperty("电梯内部编号")
    private String liftName;


}
