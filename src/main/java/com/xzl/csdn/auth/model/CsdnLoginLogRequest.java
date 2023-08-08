package com.xzl.csdn.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xzl.csdn.domain.query.BaseRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: liupu
 * @description:
 * @date: 2021/3/9
 */
@Data
public class CsdnLoginLogRequest extends BaseRequest {
	/**
	 * 告警时间:开始时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;

	/**
	 * 告警时间:结束时间
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

	/**
	 * 日志类型
	 */
	private Integer type;

	private String msg;

	/**
	 * 查询操作人和电梯
	 */
	private String condition;
	/**
	 * 应用名称
	 */
	private String appName;
}
