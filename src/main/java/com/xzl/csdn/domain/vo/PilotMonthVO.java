package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-25 16:54
 **/
@ApiModel("试点月度统计")
@Data
public class PilotMonthVO {

    @ApiModelProperty("运行里程")
    private List<BoardStatisticsVO> runTimesList;

    @ApiModelProperty("覆盖人数")
    private List<BoardStatisticsVO> useNumList;
}
