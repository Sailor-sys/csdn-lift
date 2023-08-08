package com.xzl.csdn.constant;

public class Constants {
    /**
     * 区域编码
     */
    public static final String FUYANG_TOWN_CODE = "330111";


    /**
     * 物联网获取Token接口
     */
    public static final String GET_TOKEN = "/api/v1/getToken";

    /**
     * 物联网查询电梯运行统计接口
     */
    public static final String LIFT_RUN_STATISTICS = "/api/v1/statistics/getLatestLiftRunStatistics";

    /**
     * 物联网查询电梯乘梯人数统计接口
     */
    public static final String LIFT_LATEST_PASSENGER_COUNT = "/api/v1/statistics/getLatestLiftPassengerCount";

    /**
     * 物联网告警类型统计
     */
    public static final String LIFT_ALARM_COUNT = "/api/v2/stats/alarm/count/recently";

    /**
     * 云梯token的本地线程key
     */
    public static final String YUNTI_TOKEN_KEY = "YUNTI_TOKEN_KEY";

    /**
     * 调用物联网
     */
    public static final String IOT_USER_TOKEN_KEY = "TOKEN:REMOTE:IOT:";

    /**
     * 登录错误次数
     */
    public static final String LOGINERRORNUM = "login_error_num";

    /**
     * 描述：用户状态
     */
    public static final Integer USER_ENBLE_0 = 0;//

    public static final Integer USER_ENBLE_1 = 1;//

    /**
     * 登录错误最大次数
     */
    public static final Integer LOGIN_ERROR_NUM_MAX = 3;

}
