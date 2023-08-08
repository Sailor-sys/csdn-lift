package com.xzl.csdn.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AlarmDetailVO implements Serializable{
    private String displayErrorNo;
    private String errorNo;

    private String errorType;

    private String errorTypeName;

    private String errorSourceType;

    private String errorSourceName;

    private String rescueModel;

    private String rescueModelName;

    private String locationName;

    private String liftName;

    private String regiName;

    private String locationAddress;

    private String registerCode;

    private String liftNo;

    private Integer maintId;

    private String maintName;

    private String maintChargeMan;//维保联系人、物业联系人从电梯信息中取，2019-07-10

    private String maintContactPhone;

    private Integer propertyId;

    private String propertyName;

    private String propertyChargeMan;

    private String propertyContactPhone;

    private Integer plotId;

    private String plotName;

    private String externalOrderNo;
    private String remark;

    /**
     * 救援人姓名
     */
    private String errorPersonName;
    private String exFactoryCode;
    private String exFactoryName;
    private String liftCode;
    private String liftAddress;
    private String vagueAddress;
    private String phoneTypeNames;
    private String makeCompanyName;
    private String usePlaceAddress;
    private String usePlaceName;

    private String subErrorType;
    private String subErrorTypeName;
    private String rescueTypeManits;
    private String rescueType;

    /**告警来源编号*/
    private String preAlarmNo;
    /**作为告警来源的下一个工单编号*/
    private String nextAlarmNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date nextAlarmTime;
    private String nextSubErrorType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date preAlarmTime;
    private String preSubErrorType;

    /**
     * 救援人电话
     */
    private String phoneNo;

    @JsonIgnore
    private String mContactsJson;

    @JsonIgnore
    private String pContactsJson;

    private Integer ytFlag;
    /**来自96333伤人单，伤亡数量*/
    private Integer hurtNum;
    private Integer lossNum;

    private List<String> errorNoList;

    @ApiModelProperty(value = "使用单位联系人")
    private String useUnitPerson;

    @ApiModelProperty(value = "使用单位联系方式字段")
    private String useUnitPersonPhone;

}
