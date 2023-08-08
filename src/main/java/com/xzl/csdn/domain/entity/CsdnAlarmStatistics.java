package com.xzl.csdn.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author shiqh
 * @date 2023-08-03 15:57
 * @desc 告警统计
 **/
@Data
public class CsdnAlarmStatistics {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String registerCode;

    private String deviceNumber;

    private String alarmType;

    private String locationName;

    private String locationType;

    private String innerNo;

    private String maintenanceUnitName;

    private String useUnitName;

    private Integer alarmCount;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


}
