package com.xzl.csdn.domain.entity;

import lombok.Data;

@Data
public class WxbUser {
    private Integer id;

    private String userCode;

    private String userName;

    private String userPwd;

    private Integer roleId;

    private String mobilePhone;

    private Integer userType;

    private Integer unitId;

    private Integer unitType;//1维保；2物业

    private String unitName;

    private String imageUrl;

    private Integer phoneType;

    private String roleCode;

    private Integer sex;

    private String address;

    private String remark;

    private String appCode;

}