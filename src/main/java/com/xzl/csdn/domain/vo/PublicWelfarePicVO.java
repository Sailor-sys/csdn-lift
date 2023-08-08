package com.xzl.csdn.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author shiqh
 * @date 2023-07-26 20:54
 * @desc 公益宣传图片
 **/
@Data
public class PublicWelfarePicVO {

    @ApiModelProperty("id")
    private Integer id;

    /**
     * 图片地址
     */
    @ApiModelProperty("图片地址")
    private String url;

}
