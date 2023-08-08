package com.xzl.csdn.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xzl.csdn.common.http.response.CsdnWlwLiftRunningStatistics;
import com.xzl.csdn.common.http.response.LiftPassengerCount;
import com.xzl.csdn.domain.entity.*;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.query.CsdnLiftQuery;
import com.xzl.csdn.enums.AlarmTypeEnum;
import com.xzl.csdn.enums.ErrorTypeEnum;
import com.xzl.csdn.mapper.CsdnAlarmStatisticsMapper;
import com.xzl.csdn.mapper.ZhptAlarmSiteMapper;
import com.xzl.csdn.remote.CsdnLiftRemoteApi;
import com.xzl.csdn.service.CsdnAlarmIncivilizationService;
import com.xzl.csdn.service.CsdnLiftService;
import com.xzl.csdn.service.CsdnLiftStatisticsService;
import com.xzl.csdn.service.impl.RegisterCodeService;
import com.xzl.csdn.service.impl.YunTiOpenStatisticsApi;
import com.xzl.csdn.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author shiqh
 * @date 2023-07-25 10:28
 * @desc 同步电梯信息统计
 * 每天一点同步电梯运行里程和覆盖人次
 * 每一个小时同步电梯不文明乘梯数据
 * 每天一点同步告警类型乘梯数据
 * 每九十秒处理不文明乘梯告警单
 **/
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class CsdnLiftInfoTask {

    @Autowired
    private CsdnLiftStatisticsService csdnLiftStatisticsService;
    @Autowired
    private YunTiOpenStatisticsApi yunTiOpenStatisticsApi;
    @Autowired
    private ZhptAlarmSiteMapper zhptAlarmSiteMapper;
    @Autowired
    private CsdnAlarmIncivilizationService csdnAlarmIncivilizationService;
    @Autowired
    private RegisterCodeService registerCodeService;
    @Autowired
    private CsdnLiftRemoteApi csdnLiftRemoteApi;
    @Autowired
    private CsdnAlarmStatisticsMapper csdnAlarmStatisticsMapper;
    @Autowired
    private CsdnLiftService csdnLiftService;

    @Scheduled(cron = "0 0 1 * * ? ")
    public void actionLiftRunStatistics() {
        // 同步运行里程
        syncLiftStatistics(1);
    }

    @Scheduled(cron = "0 0 2 * * ? ")
    public void actionLiftUseStatistics() {
        // 同步覆盖人次
        syncLiftStatistics(2);
    }

    /**
     * 同步电梯运行里程
     */
    public void syncLiftStatistics(Integer type) {
        List<String> registerCodeList = registerCodeService.getRegisterCodeList();
        log.info("sync lift statistics start");
        Date currentDate = new Date();
        for (String registerCode : registerCodeList) {
            List<String> list = Collections.singletonList(registerCode);

            // 查询上个月后 存在则只同步这个月的 不存在则同步之前几个月的
            CsdnLiftQuery query = new CsdnLiftQuery();
            Date previousDate = DateUtil.addMonths(currentDate, -1);
            Date firstPreviousDate = DateUtil.getFirstDayOfMonth(previousDate);
            Date lastPreviousMonth = DateUtil.getLastDayOfMonth(firstPreviousDate);
            query.setStartTime(DateUtil.getTimeByDate(firstPreviousDate));
            query.setEndTime(DateUtil.getTimeByDate(lastPreviousMonth));
            query.setType(type);
            Integer count = csdnLiftStatisticsService.querySyncCount(query);

            if (count == 0) {
                int month = DateUtil.getMonth(currentDate);
                for (int i = 0; i < month; i++) {
                    Date date = DateUtil.addMonths(currentDate, -i);
                    Date firstDayOfMonth = DateUtil.getFirstDayOfMonth(date);
                    Date lastDayOfMonth = DateUtil.getLastDayOfMonth(date);

                    if (type == 1) {
                        saveRunTimesStatistics(list, type, firstDayOfMonth, lastDayOfMonth);
                    } else if (type == 2) {
                        saveLatestLiftPassenger(list, type, firstDayOfMonth, lastDayOfMonth);
                    }
                }
            } else {
                // 同步本月
                Date firstCurrent = DateUtil.getFirstDayOfMonth(currentDate);
                Date lastCurrent = DateUtil.getLastDayOfMonth(currentDate);
                query.setStartTime(DateUtil.getTimeByDate(firstCurrent));
                query.setEndTime(DateUtil.getTimeByDate(lastCurrent));
                count = csdnLiftStatisticsService.queryCount(query);
                if (count != 0) {
                    // 本月数据不为0，则同步当天的
                    firstCurrent = DateUtil.getStartTimeByToday(new Date());
                    lastCurrent = DateUtil.getEndTimeByToday(new Date());
                }

                if (type == 1) {
                    saveRunTimesStatistics(list, type, firstCurrent, lastCurrent);
                } else if (type == 2) {
                    saveLatestLiftPassenger(list, type, firstCurrent, lastCurrent);
                }
            }
        }
        log.info("sync lift statistics end");
    }

    /**
     * 同步不文明统计(每小时同步)
     */
    @Scheduled(cron = "0 0 3 * * ? ")
    public void actionIncivilizationType() {
        List<String> registerCodeList = registerCodeService.getRegisterCodeList();

        // 当前时间
        Date currentDate = new Date();
        // 前一小时时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        Date firstCurrentDay = DateUtil.getStartTimeByToday(calendar.getTime());
        Date lastCurrentDay = DateUtil.getEndTimeByToday(currentDate);
        saveAlarmIncivilization(registerCodeList, firstCurrentDay, lastCurrentDay, true);
    }


    /**
     * 同步不文明统计(同步数据接口)
     */
    public void syncIncivilizationType() {
        CsdnBoardQuery query = new CsdnBoardQuery();
        log.info("sync Incivilization type start");
        List<String> registerCodeList = registerCodeService.getRegisterCodeList();

        Date currentDate = new Date();
        int currentDay = DateUtil.getDayOfMonth(currentDate);

        Date previousDate = DateUtil.addDays(currentDate, -1);
        Date firstPreviousDate = DateUtil.getStartTimeByToday(previousDate);
        Date lastPreviousDate = DateUtil.getEndTimeByToday(previousDate);
        query.setStartTime1(DateUtil.getTimeByDate(firstPreviousDate));
        query.setEndTime1(DateUtil.getTimeByDate(lastPreviousDate));
        Integer count = csdnAlarmIncivilizationService.queryCount(query);

        if (count == 0) {
            // 没有昨天数据，则同步前几天数据
            for (int i = 1; i < currentDay; i++) {
                Date date = DateUtil.addDays(currentDate, -i);
                Date firstCurrentDay = DateUtil.getStartTimeByToday(date);
                Date lastCurrentday = DateUtil.getEndTimeByToday(date);
                saveAlarmIncivilization(registerCodeList, firstCurrentDay, lastCurrentday, false);
            }
        } else {
            // 同步当天数据
            Date firstCurrentDay = DateUtil.getStartTimeByToday(currentDate);
            Date lastCurrentDay = DateUtil.getEndTimeByToday(currentDate);
            saveAlarmIncivilization(registerCodeList, firstCurrentDay, lastCurrentDay, true);
        }
        log.info("sync Incivilization type end");
    }

    /**
     * 处理不文明告警流程
     */
    @Scheduled(cron = "*/90 * * * * ? ")
    public void syncActionRescueProcess() {
        LambdaQueryWrapper<ZhptAlarmSite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ZhptAlarmSite::getStatus, 0);
        List<ZhptAlarmSite> zhptAlarmSites = zhptAlarmSiteMapper.selectList(queryWrapper);
        for (ZhptAlarmSite zhptAlarmSite : zhptAlarmSites) {
            log.info("action rescue process, registerCode: {}", zhptAlarmSite.getRegisterCode());
            if (Objects.isNull(zhptAlarmSite.getReceiveTime())) {
                zhptAlarmSite.setReceiveTime(new Date());
                zhptAlarmSiteMapper.updateById(zhptAlarmSite);
                continue;
            }
            if (Objects.isNull(zhptAlarmSite.getEndTime())) {
                zhptAlarmSite.setStatus(1);
                zhptAlarmSite.setEndTime(new Date());
                zhptAlarmSiteMapper.updateById(zhptAlarmSite);
            }
        }
    }

    /**
     * 执行同步告警类型
     */
    @Scheduled(cron = "0 0 4 * * ? ")
    public void actionSyncAlarmStatistics() {
        Date currentDate = new Date();
        // 前一小时时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        Date firstCurrentDay = DateUtil.getStartTimeByToday(calendar.getTime());
        Date lastCurrentDay = DateUtil.getEndTimeByToday(currentDate);
        syncAlarmStatistics(firstCurrentDay, lastCurrentDay);
    }

    /**
     * 同步告警类型
     */
    public void syncAlarmStatistics(Date startTime, Date endTime) {
        List<String> registerCodeList = registerCodeService.getRegisterCodeList();
        int index = 0;
        log.info("sync alarm statistics start");
        for (String registerCode : registerCodeList) {
            CsdnLift csdnLift = csdnLiftService.getLift(registerCode);
            for (ErrorTypeEnum value : ErrorTypeEnum.values()) {
                ++index;
                Integer liftAlarmCount = yunTiOpenStatisticsApi
                        .getLiftAlarmCount(registerCode, value.getCode(), startTime.getTime(), endTime.getTime());
                if (index == 0 ||liftAlarmCount == 0) {
                    continue;
                }
                CsdnAlarmStatistics csdnAlarmStatistics = new CsdnAlarmStatistics();
                csdnAlarmStatistics.setRegisterCode(registerCode);
                if (csdnLift != null) {
                    csdnAlarmStatistics.setDeviceNumber(csdnLift.getEquipmentCode());
                    csdnAlarmStatistics.setAlarmType(value.getName());
                    csdnAlarmStatistics.setLocationName(csdnLift.getLocationName());
                    csdnAlarmStatistics.setLocationType(csdnLift.getLocationType());
                    csdnAlarmStatistics.setInnerNo(csdnLift.getInnerNo());
                    csdnAlarmStatistics.setMaintenanceUnitName(csdnLift.getMaintenanceUnitName());
                    csdnAlarmStatistics.setUseUnitName(csdnLift.getUseUnitName());
                }
                csdnAlarmStatistics.setAlarmCount(liftAlarmCount);
                csdnAlarmStatisticsMapper.insert(csdnAlarmStatistics);
                try {
                    // 请求飞行时间
                    Thread.sleep(100 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("sync alarm statistics end");
    }

    /**
     * 同步不文明行为
     */
    public void saveAlarmIncivilization(List<String> registerCodeList, Date firstCurrentDay, Date lastCurrentDay, boolean isLast) {
        int index = 0;
        for (AlarmTypeEnum value : AlarmTypeEnum.values()) {
            Integer alarmType = value.getCode();
            for (String registerCode : registerCodeList) {
                ++index;
                try {
                    // 请求飞行时间
                    Thread.sleep(100 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer liftAlarmCount = yunTiOpenStatisticsApi
                        .getLiftAlarmCount(registerCode, alarmType, firstCurrentDay.getTime(), lastCurrentDay.getTime());
                if (index == 0 || liftAlarmCount == 0) {
                    continue;
                }

                CsdnLift csdnLift = csdnLiftService.getLift(registerCode);
                CsdnAlarmIncivilization csdnAlarmIncivilization = new CsdnAlarmIncivilization();
                csdnAlarmIncivilization.setRegisterCode(registerCode);
                if (csdnLift != null) {
                    csdnAlarmIncivilization.setLocationName(csdnLift.getLocationName());
                }
                csdnAlarmIncivilization.setAlarmType(alarmType);
                csdnAlarmIncivilization.setCount(liftAlarmCount);
                csdnAlarmIncivilization.setCreateTime(firstCurrentDay);

                csdnAlarmIncivilizationService.insert(csdnAlarmIncivilization);

                if (isLast) {
                    // 同时创建告警信息
                    buildAlarmSite(registerCode, value.getCode());
                }
            }
        }
    }

    /**
     * 创建告警信息
     */
    public void buildAlarmSite(String registerCode, Integer alarmType) {
        ZhptAlarmSite zhptAlarmSite = new ZhptAlarmSite();
        zhptAlarmSite.setRegisterCode(registerCode);
        zhptAlarmSite.setErrorType(alarmType);
        zhptAlarmSite.setNoticeTime(new Date());
        zhptAlarmSite.setStatus(0);
        zhptAlarmSiteMapper.insert(zhptAlarmSite);
    }


    /**
     * 同步运行里程
     */
    public void saveRunTimesStatistics(List<String> registerCodeList, Integer type, Date firstCurrentMonth, Date lastCurrentMonth) {
        List<CsdnWlwLiftRunningStatistics> runKmByYunTi =
                yunTiOpenStatisticsApi.getRunKmByYunTi(registerCodeList, firstCurrentMonth.getTime(), lastCurrentMonth.getTime());
        if (!runKmByYunTi.isEmpty()) {
            CsdnWlwLiftRunningStatistics runningStatistics = runKmByYunTi.get(0);
            String registerCode = registerCodeList.get(0);
            BigDecimal runDistance = runningStatistics.getRunDistance();
            CsdnLiftStatistics csdnLiftStatistics = new CsdnLiftStatistics();
            csdnLiftStatistics.setRegisterCode(registerCode);
            csdnLiftStatistics.setType(type);
            csdnLiftStatistics.setCount(runDistance);
            csdnLiftStatistics.setStatisticsTime(firstCurrentMonth);

            CsdnLift csdnLift = csdnLiftService.getLift(registerCode);
            if (csdnLift != null) {
                csdnLiftStatistics.setLocationName(csdnLift.getLocationName());
                csdnLiftStatistics.setLocationType(csdnLift.getLocationType());
                csdnLiftStatistics.setStreetCode(csdnLift.getStreetCode());
                csdnLiftStatistics.setMaintenanceUnitName(csdnLift.getMaintenanceUnitName());
                csdnLiftStatistics.setUseUnitName(csdnLift.getUseUnitName());
                if (csdnLift.getProductionDate() != null) {
                    csdnLiftStatistics.setMadeTime(csdnLift.getProductionDate());
                }
            }

            csdnLiftStatisticsService.insert(csdnLiftStatistics);
        }
    }

    /**
     * 同步覆盖人次
     */
    public void saveLatestLiftPassenger(List<String> registerCodeList, Integer type, Date firstCurrentMonth, Date lastCurrentMonth) {
        List<LiftPassengerCount> latestLiftPassengerCount =
                yunTiOpenStatisticsApi.getLatestLiftPassengerCount(registerCodeList, firstCurrentMonth.getTime(), lastCurrentMonth.getTime());
        if (!latestLiftPassengerCount.isEmpty()) {
            LiftPassengerCount liftPassengerCount = latestLiftPassengerCount.get(0);
            CsdnLiftStatistics csdnLiftStatistics = new CsdnLiftStatistics();
            String registerCode = registerCodeList.get(0);
            csdnLiftStatistics.setRegisterCode(registerCode);
            csdnLiftStatistics.setType(type);
            csdnLiftStatistics.setCount(BigDecimal.valueOf(Long.parseLong(liftPassengerCount.getCount())));
            csdnLiftStatistics.setStatisticsTime(firstCurrentMonth);

            CsdnLift csdnLift = csdnLiftService.getLift(registerCode);
            if (csdnLift != null) {
                csdnLiftStatistics.setLocationName(csdnLift.getLocationName());
                csdnLiftStatistics.setLocationType(csdnLift.getLocationType());
                csdnLiftStatistics.setStreetCode(csdnLift.getStreetCode());
                csdnLiftStatistics.setInnerNo(csdnLift.getInnerNo());
                csdnLiftStatistics.setMaintenanceUnitName(csdnLift.getMaintenanceUnitName());
                csdnLiftStatistics.setUseUnitName(csdnLift.getUseUnitName());
                if (csdnLift.getProductionDate() != null) {
                    csdnLiftStatistics.setMadeTime(csdnLift.getProductionDate());
                }
            }
            csdnLiftStatisticsService.insert(csdnLiftStatistics);
        }
    }


}
