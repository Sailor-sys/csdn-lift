package com.xzl.csdn.domain.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRequest {

    @ApiModelProperty(hidden=true)
    private int pageIndex = 1;

    @ApiModelProperty(hidden=true)
    private int pageSize = 20;


    @ApiModelProperty(hidden=true)
    private String startTime1;
    @ApiModelProperty(hidden=true)
    private String endTime1;

    @ApiModelProperty(hidden=true)
    private String startTime2;
    @ApiModelProperty(hidden=true)
    private String endTime2;

    @ApiModelProperty(hidden=true)
    private String startTime3;
    @ApiModelProperty(hidden=true)
    private String endTime3;
}
