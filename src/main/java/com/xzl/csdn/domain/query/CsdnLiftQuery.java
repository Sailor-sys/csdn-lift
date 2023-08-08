package com.xzl.csdn.domain.query;

import lombok.Data;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-08-02 15:36
 * @desc 查询电梯条件对象
 **/
@Data
public class CsdnLiftQuery {

    private String registerCode;
    private String townCode;
    private Integer useStatus;
    private String cityCode;
    private String streetCode;
    private Integer connectNetFlag;
    private Integer liftType;
    private String locationType;
    private String locationName;
    private List<String> registerCodeList;

    /**
     * 查询生产时间
     */
    private String startTime;
    private String endTime;

    private Integer type;

}
