package com.xzl.csdn.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-26 20:50
 * @desc 公益宣传
 **/
@Data
public class PublicWelfareVO {

    @ApiModelProperty("id")
    private Integer id;

    /**
     * 宣传品种
     */
    @ApiModelProperty("宣传品种")
    private Integer publicityBreed;

    /**
     * 播放
     */
    @ApiModelProperty("播放")
    private String playBack;

    /**
     * 宣传时长
     */
    @ApiModelProperty("宣传时长")
    private String publicityTime;

    /**
     * 覆盖人次
     */
    @ApiModelProperty("覆盖人次")
    private Integer usePeople;

    @ApiModelProperty("图片地址")
    private List<String> picUrlList;

}
