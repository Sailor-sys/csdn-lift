package com.xzl.csdn.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xzl.csdn.common.ThreadLocalManager;
import com.xzl.csdn.common.exception.BusinessException;
import com.xzl.csdn.common.http.HttpClientComponent;
import com.xzl.csdn.common.http.response.*;
import com.xzl.csdn.constant.Constants;
import com.xzl.csdn.service.CsdnLiftService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shiqh
 * @date 2023-07-25 14:35
 * @desc 云梯开放平台接口
 **/
@Slf4j
@Service
public class YunTiOpenStatisticsApi {

    @Autowired
    private HttpClientComponent httpClientComponent;

    @Value("${yunti.open.url}")
    private String yuntiOpenUrl;
    @Value("${yunti.open.appkey}")
    private String appkey;
    @Value("${yunti.open.secret}")
    private String appSecret;

    /**
     * 从云梯获取运行数据
     * 时间是毫秒
     */
    public List<CsdnWlwLiftRunningStatistics> getRunKmByYunTi(List<String> registerCodeList, long beginTime, long endTime) {
        String token = getYunTiToken();
        String url = yuntiOpenUrl + Constants.LIFT_RUN_STATISTICS + "?access_token=" + token;

        Map<String, Object> paramMap = new HashMap<>(3);
        paramMap.put("registerCodes", registerCodeList);
        paramMap.put("beginTime", beginTime / 1000);
        paramMap.put("endTime", endTime / 1000);
        String result = null;
        try {
            String httpResult = httpClientComponent.doHttpPostRequest(url, paramMap, new HashMap<>());
            if (!StringUtils.isEmpty(httpResult)) {
                result = httpResult;
            }
        } catch (Exception e) {
            log.error("get lift run info error: {}", e);
            throw new BusinessException("获取电梯运行数据失败");
        }
        List<CsdnWlwLiftRunningStatistics> bigDataScoreDTOList = new ArrayList<>();
        BigDataResponseDTO response = JSONObject.parseObject(result, BigDataResponseDTO.class);
        if (response != null && "22000002".equals(response.getCode())) {
            ThreadLocalManager.remove(Constants.YUNTI_TOKEN_KEY);
            getRunKmByYunTi(registerCodeList, beginTime, endTime);
        }
        if (response == null || response.getData() == null) {
            return bigDataScoreDTOList;
        }
        if ("22000999".equals(response.getCode())) {
            getRunKmByYunTi(registerCodeList, beginTime, endTime);
        }
        bigDataScoreDTOList =
                JSONObject.parseArray(JSONObject.toJSONString(response.getData()), CsdnWlwLiftRunningStatistics.class);
        return bigDataScoreDTOList;
    }


    /**
     * 从云梯获取乘梯人数
     */
    public List<LiftPassengerCount> getLatestLiftPassengerCount(List<String> registerCodeList, long beginTime, long endTime) {
        String token = getYunTiToken();
        String url = yuntiOpenUrl + Constants.LIFT_LATEST_PASSENGER_COUNT + "?access_token=" + token;

        Map<String, Object> paramMap = new HashMap<>(3);
        paramMap.put("registerCodes", registerCodeList);
        paramMap.put("beginTime", beginTime / 1000);
        paramMap.put("endTime", endTime / 1000);
        String result = null;
        try {
            String httpResult = httpClientComponent.doHttpPostRequest(url, paramMap, new HashMap<>(0));
            if (!StringUtils.isEmpty(httpResult)) {
                result = httpResult;
            }
        } catch (Exception e) {
            log.error("get lift run info error: {}", e);
            throw new BusinessException("获取电梯运行数据失败");
        }
        List<LiftPassengerCount> bigDataScoreDTOList = new ArrayList<>();
        BigDataResponseDTO response = JSONObject.parseObject(result, BigDataResponseDTO.class);
        if (response != null && "22000002".equals(response.getCode())) {
            ThreadLocalManager.remove(Constants.YUNTI_TOKEN_KEY);
            getLatestLiftPassengerCount(registerCodeList, beginTime, endTime);
        }
        if (response == null || response.getData() == null) {
            return bigDataScoreDTOList;
        }
        if ("22000999".equals(response.getCode())) {
            getLatestLiftPassengerCount(registerCodeList, beginTime, endTime);
        }
        bigDataScoreDTOList =
                JSONObject.parseArray(JSONObject.toJSONString(response.getData()), LiftPassengerCount.class);
        return bigDataScoreDTOList;
    }

    /**
     * 从云梯获取告警类型数量
     */
    public Integer getLiftAlarmCount(String registerCode, Integer alarmType, long beginTime, long endTime) {
        String token = getYunTiToken();
        String url = yuntiOpenUrl + Constants.LIFT_ALARM_COUNT + "?access_token=" + token;

        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("beginTime", beginTime / 1000);
        paramMap.put("endTime", endTime / 1000);
        paramMap.put("alarmType", alarmType);
        paramMap.put("registerCode", registerCode);
        String result = null;
        try {
            String httpResult = httpClientComponent.doHttpPostRequest(url, paramMap, new HashMap<>(0));
            if (!StringUtils.isEmpty(httpResult)) {
                result = httpResult;
            }
        } catch (Exception e) {
            log.error("get lift run info error: {}", e);
            throw new BusinessException("获取电梯运行数据失败");
        }
        BigDataResponseDTO response = JSONObject.parseObject(result, BigDataResponseDTO.class);
        // 22000002 云梯token失效
        if (response != null && "22000002".equals(response.getCode())) {
            ThreadLocalManager.remove(Constants.YUNTI_TOKEN_KEY);
            getLiftAlarmCount(registerCode, alarmType, beginTime, endTime);
        }
        if (response == null || response.getData() == null) {
            return 0;
        }
        if ("22000999".equals(response.getCode())) {
            getLiftAlarmCount(registerCode, alarmType, beginTime, endTime);
        }
        RecentlyResponse recentlyResponse = JSONObject.parseObject(JSONObject.toJSONString(response.getData().get(0)), RecentlyResponse.class);
        return recentlyResponse.getCount() == null ? 0 : recentlyResponse.getCount();
    }


    /**
     * 获取云梯token
     */
    public String getYunTiToken() {
        Object token = ThreadLocalManager.get(Constants.YUNTI_TOKEN_KEY);
        if (token != null) {
            return String.valueOf(token);
        }

        String tokenUrl = yuntiOpenUrl + Constants.GET_TOKEN;
        Map<String, String> paramMap = new HashMap<>(2);
        paramMap.put("AppKey", appkey);
        paramMap.put("AppSecret", appSecret);

        String result = null;
        try {
            String httpResult = httpClientComponent.doHttpPostRequest(tokenUrl, JSON.toJSONString(paramMap));
            if (!StringUtils.isEmpty(httpResult)) {
                result = httpResult;
            }
        } catch (Exception e) {
            log.error("get yunTi token error: {}", e);
            throw new BusinessException("获取云梯token失败");
        }
        BigDataResponseDTO response = JSONObject.parseObject(result, BigDataResponseDTO.class);
        if (response == null || response.getData() == null) {
            return null;
        }
        List<TokenData> tokenDataList = JSONObject.parseArray(String.valueOf(response.getData()), TokenData.class);
        String accessToken = tokenDataList.get(0).getAccessToken();
        ThreadLocalManager.bind(Constants.YUNTI_TOKEN_KEY, accessToken);
        return accessToken;
    }

}
