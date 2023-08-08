package com.xzl.csdn.domain.entity;

import lombok.Data;

@Data
public class LonLat {
    private String lon;
    private String lat;

    public LonLat(String lon, String lat){
        this.lat  = lat;
        this.lon = lon;
    }
}
