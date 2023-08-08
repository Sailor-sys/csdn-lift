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
 * @date 2023-07-25 17:42
 * @desc 不文明统计
 **/
@Data
public class CsdnAlarmIncivilization {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 注册代码
     */
    private String registerCode;

    /**
     * 场所名称
     */
    private String locationName;

    /**
     * 告警类型
     */
    private Integer alarmType;

    /**
     * 告警数量
     */
    private Integer count;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}
