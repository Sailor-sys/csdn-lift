package com.xzl.csdn.domain.entity;

import lombok.Data;

@Data
public class CsdnLift {

    private Integer id;

    private String registerCode;

    private String equipmentCode;

    private String modelNumber;

    private String provinceCode;

    private String provinceName;

    private String cityCode;

    private String cityName;

    private String townCode;

    private String townName;

    private String streetCode;

    private String streetName;

    private String locationType;

    private String locationName;

    private String innerNo;

    private String lon;

    private String lat;

    private Integer useStatus;
    private String useStatusName;


    private String detailAddress;

    private Integer useUnitId;

    private String useUnitCode;

    private String useUnitName;

    private String useUnitContacter;

    private String useUnitPhone;

    private String safeManager;

    private String safeManagerPhone;

    private Integer maintenanceUnitId;

    private String maintenanceUnitCode;

    private String maintenanceUnitName;

    private String maintenanceUnitStar;

    private String maintenanceUnitQualification;

    private String maintenanceUnitContacter;

    private String maintenanceUnitPhone;

    private Integer manufactureUnitId;

    private String manufactureUnitName;

    private String manufactureUnitQualification;

    private String productionDate;

    private String inspectionUnitName;

    private String inspectDate;

    private String inspectResult;

    private String nextInspectDate;

    private String installationUnitName;

    private String installationUnitQualification;

    private String installDate;



    private String transformUnitName;

    private String transformUnitQualification;

    private String transformDate;

    private String overhaulUnitName;

    private String overhaulUnitQualification;

    private String overhaulDate;

    private String liftType;

    private String liftBrand;

    private String liftSpeed;

    private String liftRatedLoad;

    private String liftCs;

    private String liftZs;

    private String liftMs;

    private String maintenanceType;

    private String yyjModel;

    private String xsqModel;

    private String ddjModel;

    private String kzgModel;

    private String tmsModel;

    private String jmsModel;

    private String kzpModel;

    private String aqqModel;

    private String ywydbhzzModel;

    private String sxcsbhzzModel;
    private String createTime;

    private String updateTime;
    private Boolean maintenanceOverdualFlag;
    private Boolean checkOverdualFlag;
    private String errorSourceName;

    private String latestMaintenanceDate;
    private Integer healthCode;
    private String exfactoryCode;


    private String connectNetFlag;
}