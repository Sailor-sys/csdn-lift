package com.xzl.csdn.auth.service.impl;

import com.xzl.csdn.auth.service.IgnoreUrlService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XZL
 * @desc 过滤url
 */
@Service
public class IgnoreUrlServiceImpl implements IgnoreUrlService {
    @Override
    public List<String> methodGetIgnoreUrl() {
        return new ArrayList<>();
    }

    @Override
    public List<String> allMethodIgnoreUrl() {
        ArrayList<String> ignoreUrl = new ArrayList<>();
        ignoreUrl.add("/csdn/home/doLogin");
        ignoreUrl.add("/csdn/home/getImageCode");
        ignoreUrl.add("/csdn/home/sms/getCode");
        ignoreUrl.add("/csdn/home/getToken");
        ignoreUrl.add("/csdn/home/getSystemAreaInfo");
        ignoreUrl.add("/dt/**");
        ignoreUrl.add("/actuator/health");
        // swagger
        ignoreUrl.add("/doc.html");
        ignoreUrl.add("/webjars/**");
        ignoreUrl.add("/swagger-resources/**");
        ignoreUrl.add("/favicon.ico");
        ignoreUrl.add("/v2/api-docs");

        // 同步数据接口
        ignoreUrl.add("/csdn/board/syncLiftStatistics");
        ignoreUrl.add("/csdn/board/syncIncivilizationType");
        ignoreUrl.add("/csdn/board/syncAlarmStatistics");
//        ignoreUrl.add("/csdn/board/**");

        return ignoreUrl;
    }
}
