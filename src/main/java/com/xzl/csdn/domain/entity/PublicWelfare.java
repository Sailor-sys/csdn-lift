package com.xzl.csdn.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author shiqh
 * @date 2023-07-26 20:50
 * @desc 公益宣传
 **/
@Data
public class PublicWelfare {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 宣传品种
     */
    private Integer publicityBreed;

    /**
     * 播放
     */
    private String playBack;

    /**
     * 宣传时长
     */
    private String publicityTime;

    /**
     * 覆盖人次
     */
    private Integer usePeople;

    /**
     * 图片地址1
     */
    private String picUrls;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
}
