package com.xzl.csdn.auth.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author auto-generator
 * @since 2020-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CsdnLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private Integer loginType;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String ipAddress;

    /**
     * 0:成功，1：失败
     */
    private Integer loginResult;

    /**
     * 登陆失败消息
     */
    private String errorMsg;

	private String phone;

    private Integer type;
    private String appName;
}
