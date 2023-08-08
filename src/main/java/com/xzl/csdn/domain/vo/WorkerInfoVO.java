package com.xzl.csdn.domain.vo;

import com.xzl.csdn.domain.entity.LonLat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-27 16:14
 * @desc 救援人员轨迹信息
 **/
@Data
@ApiModel("救援人员人员信息")
public class WorkerInfoVO {

    @ApiModelProperty("维保人员姓名")
    private String name;

    @ApiModelProperty("维保人员手机号")
    private String phone;

    @ApiModelProperty("终点位置")
    private Route endPoint;

    @ApiModelProperty("路径信息")
    private List<Route> routeList;

}
