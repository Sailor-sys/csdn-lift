package com.xzl.csdn.support;

public enum ApiStatus {
    /**
     * 消除黄线
     */
    OK(100, "成功"),
    SUCCESS(200, "成功"),
    LOGIN_REQUIRED(102, "尚未登录，请登录"),
    PARAM_DATA_ERROR(203, "参数错误"),
    PARAM_DATA_FORMAT_ERROR(205, "参数解析异常，请检查参数格式"),
    NO_DATA_ERROR(204,"获取不到相关数据"),
    UNAUTHORIZED(401, "没有访问权限"),
    INTERNAL_SERVER_ERROR(500, "服务器错误"),
    INTERNAL_DATA_BINGING(502, "服务器错误，数据绑定出错"),
    WORK_ORDER_FINISH(1001, "工单已完成，请勿重复提交！"),
    MAINT_TYPE_CHANGED(1002, "所选维保类型和服务器不一致,请重新选择"),
    WORKER_ORDER_OVER(1003, "其他人已完成救援"),
    MANAGER_ORDER_REJECT(1004, "负责人已拒绝接警！"),
    TRYLOGINAGAIN(1005, "没有访问权限,请尝试重新登录"),
    FZR_ACCEPTED(1006, "已有负责人接警"),
    FZR_REJECTED(1007, "已有负责人拒警"),
    ORDER_CANCEL(1008, "工单已撤销"),
    ORDER_ACCEPTED(1009, "分平台已接警，不能撤销"),
    ORDER_DISPOSETYPE_CHANGE(1010, "已转为人工处置"),
    REAL_VIDEO_ERROR(1011, "获取实时视频出错"),
    HIS_VIDEO_ERROR(1012, "获取历史视频出错"),
    LIFT_TALK_ERROR(1013, "获取对讲信息出错"),
    MILEAGE_ERROR(1014, "获取最近运行里程出错"),
    DOOR_SWITCH_TIME_ERROR(1015, "获取最近厅门开关次数出错"),
    RUNNING_TIME_ERROR(1016, "获取最近运行次数出错"),
    PATROL_NO_LIFT(1017, "没有获取到对应的电梯信息，请联系管理员"),
    PATROL_NO_PLACE(1018, "没有获取到对应的场所信息，请联系管理员"),
	PATROL_NO_MAINT(1019, "此电梯不属于所属维保，请联系管理员"),
    FINISH_REPAIR_ERROR(1020, "本次维修已经由他人维修完成；无须再进行维修"),
    QUERY_YUNTI_LIFT_ERROR(1021, "获取云梯电梯信息出错"),
    LINK_ADDRESS_EXIST(1022, "该地区的链接已生成"),
    LINK_ADDRESS_OUTTIME(1023, "该链接已过期"),
    FETCH_LOGIN_CODE_TOO_QUICK(1024, "获取登录验证码太频繁"),
    LOGIN_CODE_EXPIRE(1025, "登录验证码过期"),
    ACTIVITY_END(1026, "评分已结束"),
    LINK_ADDRESS_DELETE(1027, "该链接已失效"),
    LIFT_NOT_EXIT(1028, "电梯不存在");

    private int status;
    private String message;

    ApiStatus(int errorCode, String message) {
        this.status = errorCode;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
