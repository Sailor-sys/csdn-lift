package com.xzl.csdn.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shiqh
 * @date 2023-07-25 14:13
 * @desc 电梯统计数据
 **/
@Data
public class CsdnLiftStatistics {

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
     * 场所类型
     */
    private String locationType;

    /**
     * 街道编号
     */
    private String streetCode;

    /**
     * 内部编号
     */
    private String innerNo;

    /**
     * 维保单位
     */
    private String maintenanceUnitName;

    /**
     * 使用单位
     */
    private String useUnitName;

    /**
     * 制造时间
     */
    private String madeTime;

    /**
     * 次数
     */
    private BigDecimal count;

    /**
     * 1：运行里程  2：覆盖人次
     */
    private Integer type;

    /**
     * 统计时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date statisticsTime;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}
