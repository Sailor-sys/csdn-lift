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
 * @date 2023-07-27 17:00
 * @desc 告警流程
 **/
@Data
public class ZhptAlarmSite {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 注册代码
     */
    private String registerCode;

    /**
     * 告警类型
     */
    private Integer errorType;

    /**
     * 通知时间
     */
    private Date noticeTime;

    /**
     * 接警时间
     */
    private Date receiveTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 0:处理中，1：结束
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}
