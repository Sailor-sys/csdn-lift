package com.xzl.csdn.domain.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CsdnUser {
    private Integer id;

    private String username;

    private String nickname;

    private String password;

    private Date passwordUpdateTime;

    private String address;

    private Integer unitId;

    private Integer unitType;

    private String phone;

    private Integer enable;

    private Integer deleteTag;

    private Integer loginTag;

    private String townCode;

    private String townName;

    private String unitName;

    private String roleCode;

    private String roleName;

    private Integer roleId;

    private String streetCode;

    private String streetName;

	/**
	 * 实时监控正色，0反色，1正色
	 */
	private Integer actualPureColor;

	/**
	 * 监控回放正色，0反色，1正色
	 */
	private Integer playbackPureColor;

    /**
     * 创建用户
     */
    private String createUser;
    private String deptId;
    private String agentId;

	private Date createTime;

	private Date updateTime;

    private boolean userIsAdmin;

    private String useUnitName;
}