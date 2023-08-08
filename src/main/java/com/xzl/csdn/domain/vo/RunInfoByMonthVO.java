package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-20 17:10
 * @desc 月度运行数据统计
 **/
@Data
@ApiModel("月度运行数据统计")
public class RunInfoByMonthVO {

    @ApiModelProperty("月度运行里程")
    private List<BoardStatisticsVO> monthRunInfoList;

    @ApiModelProperty("月度覆盖人次")
    private List<BoardStatisticsVO> monthUsePeopleList;

}
