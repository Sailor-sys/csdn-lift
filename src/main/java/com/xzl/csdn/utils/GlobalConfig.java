package com.xzl.csdn.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置
 *
 * @author gll
 * @date 2020/5/25 17:24
 */
@Configuration
public class GlobalConfig {

    private static String IOT_SERVER_UTL;

    @Value("${iot.server.url}")
    private void setIotServerUrl(String iotServerUrl) {
        IOT_SERVER_UTL = iotServerUrl;
    }

    public static String getIotServerUrl() {
        return IOT_SERVER_UTL;
    }
}
