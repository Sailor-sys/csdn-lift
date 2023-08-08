package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiqh
 * @date 2023-07-28 10:49
 * @desc 点位
 **/
@Data
public class Route {

    @ApiModelProperty("经度")
    private String lon;

    @ApiModelProperty("纬度")
    private String lat;

    public Route(String lon, String lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public Route() {
    }
}
