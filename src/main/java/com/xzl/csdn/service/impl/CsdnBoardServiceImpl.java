package com.xzl.csdn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.tuyang.beanutils.BeanCopyUtils;
import com.xzl.csdn.auth.handler.LoginProcessHandler;
import com.xzl.csdn.common.exception.BusinessException;
import com.xzl.csdn.common.http.response.CsdnWlwLiftRunningStatistics;
import com.xzl.csdn.common.http.response.LiftPassengerCount;
import com.xzl.csdn.domain.entity.*;
import com.xzl.csdn.domain.query.*;
import com.xzl.csdn.domain.vo.*;
import com.xzl.csdn.enums.AlarmTypeEnum;
import com.xzl.csdn.enums.ErrorTypeEnum;
import com.xzl.csdn.enums.IncivilizationTypeEnum;
import com.xzl.csdn.mapper.*;
import com.xzl.csdn.remote.CsdnLiftRemoteApi;
import com.xzl.csdn.remote.LiftAgentRemoteApi;
import com.xzl.csdn.remote.request.LoginParam;
import com.xzl.csdn.remote.response.DataResponse;
import com.xzl.csdn.remote.response.DataTransfer;
import com.xzl.csdn.remote.response.LoginResponse;
import com.xzl.csdn.service.CsdnAlarmIncivilizationService;
import com.xzl.csdn.service.CsdnBoardService;
import com.xzl.csdn.service.CsdnLiftStatisticsService;
import com.xzl.csdn.utils.DateUtil;
import com.xzl.zq.csdn.response.CsdnBaseResponse;
import com.xzl.zq.remote.common.token.RedisTokenCacheService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shiqh
 * @date 2023-07-19 11:19
 **/
@Slf4j
@Service
public class CsdnBoardServiceImpl implements CsdnBoardService {

    @Autowired
    private CsdnBoardMapper csdnBoardMapper;
    @Autowired
    private PublicWelfareMapper publicWelfareMapper;
    @Autowired
    private ZhptAlarmSiteMapper zhptAlarmSiteMapper;
    @Autowired
    private RegisterCodeService registerCodeService;
    @Autowired
    private YunTiOpenStatisticsApi yunTiOpenStatisticsApi;
    @Autowired
    private CsdnLiftStatisticsService csdnLiftStatisticsService;
    @Autowired
    private CsdnAlarmIncivilizationService csdnAlarmIncivilizationService;
    @Autowired
    private CsdnLiftRemoteApi csdnLiftRemoteApi;
    @Autowired
    private LiftAgentRemoteApi liftAgentRemoteApi;
    @Autowired
    private CsdnAlarmStatisticsMapper csdnAlarmStatisticsMapper;
    @Autowired
    private RedisTokenCacheService redisTokenCacheService;

    private final static String LIFT_AGENT_TOKEN = "LIFT_AGENT_TOKEN";

    @Override
    public RescueNumStatisticsVO getRescueNumStatistics(CsdnBoardQuery query) {
        initLiftAgentToken(query.getTownCode());
        return liftAgentRemoteApi.getRescueNumStatistics(query);
    }

    @Override
    public List<BoardStatisticsVO> getMalfunctionMonth(CsdnBoardQuery query) {
        initLiftAgentToken(query.getTownCode());

        Date currentDate = new Date();
        Date firstDayOfMonth = DateUtil.getFirstDayOfMonth(currentDate);
        Date lastDayOfMonth = DateUtil.getLastDayOfMonth(currentDate);
        query.setStartTime1(DateUtil.getTimeByDate(firstDayOfMonth));
        query.setEndTime1(DateUtil.getTimeByDate(lastDayOfMonth));
        if (query.getType() != null && query.getType() == 1) {
            query.setErrorType("9000001");

            List<BoardStatisticsVO> malfunctionMonth = liftAgentRemoteApi.getMalfunctionMonth(query);
            return buildDateList(malfunctionMonth, currentDate);
        } else if (query.getType() != null && query.getType() == 2) {
            List<BoardStatisticsVO> alarmStatisticsMonth = csdnAlarmStatisticsMapper.getAlarmStatisticsMonth(query);
            return buildDateList(alarmStatisticsMonth, currentDate);
        } else if (query.getType() != null && query.getType() == 3) {
            List<BoardStatisticsVO> hinderLiftList = csdnAlarmIncivilizationService.getHinderLiftList(query);
            return buildDateList(hinderLiftList, currentDate);
        } else if (query.getType() != null && query.getType() == 4) {
            List<BoardStatisticsVO> hinderLiftList = csdnAlarmIncivilizationService.getHinderLiftList(query);
            return buildDateList(hinderLiftList, currentDate);
        }
        return null;
    }

    @Override
    public PilotMonthVO getPilotMonth(CsdnBoardQuery query) {
        PilotMonthVO pilotMonthVO = new PilotMonthVO();

        Date currentDate = new Date();
        Date firstDayOfYear = DateUtil.getFirstDayOfYear(currentDate);
        Date lastDayOfYear = DateUtil.getLastDayOfYear(currentDate);
        query.setStartTime1(DateUtil.getTimeByDate(firstDayOfYear));
        query.setEndTime1(DateUtil.getTimeByDate(lastDayOfYear));
        query.setType(1);
        List<BoardStatisticsVO> runTimesList = csdnLiftStatisticsService.selectListStatistics(query);
        pilotMonthVO.setRunTimesList(runTimesList);

        query.setType(2);
        List<BoardStatisticsVO> useNumList = csdnLiftStatisticsService.selectListStatistics(query);
        pilotMonthVO.setUseNumList(useNumList);
        return pilotMonthVO;
    }

    @Override
    public MalfunctionRateVO getMalfunctionStatistics(CsdnBoardQuery query) {
        MalfunctionRateVO malfunctionRateVO = new MalfunctionRateVO();
        // 查出各类型故障数量
        List<BoardStatisticsVO> boardStatisticsVOS = csdnAlarmStatisticsMapper.getAlarmStatistics(query);
        // 类型故障总数
        int count = boardStatisticsVOS.stream().mapToInt(boardStatisticsVO -> Integer.parseInt(boardStatisticsVO.getCode())).sum();
        for (BoardStatisticsVO boardStatisticsVO : boardStatisticsVOS) {
            if (count == 0) {
                return malfunctionRateVO;
            }
            // 计算比例
            String rate = new BigDecimal(boardStatisticsVO.getCode())
                    .divide(new BigDecimal(String.valueOf(count)), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100)).toString();
            if (boardStatisticsVO.getName().equals(ErrorTypeEnum.ALARM_TYPE_KR.getName())) {
                malfunctionRateVO.setTrappedRate(rate);
            } else if (boardStatisticsVO.getName().equals(ErrorTypeEnum.ALARM_TYPE_TT.getName())) {
                malfunctionRateVO.setStopRate(rate);
            } else if (boardStatisticsVO.getName().equals(ErrorTypeEnum.ALARM_TYPE_FW.getName())) {
                malfunctionRateVO.setResetRate(rate);
            } else if (boardStatisticsVO.getName().equals(ErrorTypeEnum.ALARM_TYPE_JT.getName())) {
                malfunctionRateVO.setExigencyRate(rate);
            } else if (boardStatisticsVO.getName().equals(ErrorTypeEnum.ALARM_TYPE_ZD.getName())) {
                malfunctionRateVO.setShakeRate(rate);
            } else if (boardStatisticsVO.getName().equals(ErrorTypeEnum.ALARM_TYPE_CSJDM.getName())) {
                malfunctionRateVO.setShieldRate(rate);
            }
        }

        return malfunctionRateVO;
    }


    @Override
    public List<MalfunctionFromVO> getMalfunctionFrom(CsdnBoardQuery query) {
        buildTime(query);
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return csdnAlarmStatisticsMapper.queryAlarmStatisticsList(query);
    }

    @Override
    public PilotLiftVO getPilotLift(CsdnBoardQuery query) {
        Date currentDate = new Date();
        Date firstDayOfYear = DateUtil.getFirstDayOfYear(currentDate);
        Date lastDayOfYear = DateUtil.getLastDayOfYear(currentDate);

        PilotLiftVO pilotLiftVO = new PilotLiftVO();

        // 查询的电梯注册代码
        List<String> registerCodeList = registerCodeService.getRegisterCodeList();

        int yearRunKm = 0;
        int yearUsePeopleNum = 0;
        if (registerCodeList.size() > 100) {
            int index = 0;
            int num = 100;
            while (index < registerCodeList.size()) {
                int toIndex = Math.min((index + num), registerCodeList.size());
                List<String> list = registerCodeList.subList(index, toIndex);
                // 运行数据
                List<CsdnWlwLiftRunningStatistics> wlwLiftRunningList = yunTiOpenStatisticsApi.
                        getRunKmByYunTi(list, firstDayOfYear.getTime(), lastDayOfYear.getTime());
                yearRunKm = wlwLiftRunningList.stream().mapToInt(runningStatistics ->
                        Math.round(Float.parseFloat(String.valueOf(runningStatistics.getRunDistance())))).sum();

                // 乘梯人数
                List<LiftPassengerCount> latestLiftPassengerList = yunTiOpenStatisticsApi.
                        getLatestLiftPassengerCount(list, firstDayOfYear.getTime(), lastDayOfYear.getTime());
                yearUsePeopleNum = latestLiftPassengerList.stream().mapToInt(runningStatistics ->
                        Integer.parseInt(runningStatistics.getCount())).sum();
                index += num;
            }
        }

        // 运行数据
        pilotLiftVO.setYearRunKm(yearRunKm);

        // 乘梯人数
        pilotLiftVO.setYearUsePeopleNum(yearUsePeopleNum);

        // 不文明乘梯
        Integer yearNoCivilizedLadder = csdnAlarmIncivilizationService.getAlarmCount(query);
        pilotLiftVO.setYearNoCivilizedLadder(yearNoCivilizedLadder);

        return pilotLiftVO;
    }

    @Override
    public List<MalfunctionFromVO> getRunInfo(CsdnBoardQuery query) {
        buildTime(query);
        return csdnLiftStatisticsService.getRunInfoList(query);
    }

    @Override
    public CsdnLiftNumVO getLiftNum(CsdnBoardQuery query) {
        CsdnLiftNumVO csdnLiftNumVO = new CsdnLiftNumVO();
        // 电梯数量查询条件
        CsdnLiftQuery csdnLiftQuery = buildGetLiftNumQuery(query);
        CsdnBaseResponse<List<CsdnLift>> listCsdnBaseResponse = csdnLiftRemoteApi.queryLiftList(csdnLiftQuery);
        if (listCsdnBaseResponse.getData() != null) {
            csdnLiftNumVO.setTotalCount(listCsdnBaseResponse.getData().size());
        } else {
            csdnLiftNumVO.setTotalCount(0);
        }

        // 覆盖人次
        csdnLiftQuery.setType(2);
        Integer coverageCount = csdnLiftStatisticsService.queryCount(csdnLiftQuery);
        csdnLiftNumVO.setCoverageCount(coverageCount);
        return csdnLiftNumVO;
    }

    @Override
    public List<BoardStatisticsVO> getRealTimeAlarm(CsdnBoardQuery query) {
        Date currentDate = new Date();
        Date startTime;
        Date endTime;
        if (query.getAlarmFlag() != null && query.getAlarmFlag() == 2) {
            startTime = DateUtil.getFirstDayOfMonth(currentDate);
            endTime = DateUtil.getLastDayOfMonth(currentDate);
            query.setStartTime1(DateUtil.getTimeByDate(startTime));
            query.setEndTime1(DateUtil.getTimeByDate(endTime));
        } else if (query.getAlarmFlag() != null && query.getAlarmFlag() == 3) {
            Date startTimeByToday = DateUtil.getStartTimeByToday(currentDate);
            startTime = DateUtil.addDays(startTimeByToday, -1);
            Date endTimeByToday = DateUtil.getEndTimeByToday(currentDate);
            endTime = DateUtil.addDays(endTimeByToday, -1);
            query.setStartTime1(DateUtil.getTimeByDate(startTime));
            query.setEndTime1(DateUtil.getTimeByDate(endTime));
        } else {
            List<String> alarmTypeList = Arrays.stream(IncivilizationTypeEnum.values())
                    .map(incivilizationTypeEnum -> String.valueOf(incivilizationTypeEnum.getCode())).collect(Collectors.toList());
            query.setAlarmTypeList(alarmTypeList);
        }
        List<BoardStatisticsVO> boardStatisticsVOS = csdnAlarmIncivilizationService.queryListByType(query);
        Map<String, String> map = boardStatisticsVOS.stream().collect(Collectors.toMap(BoardStatisticsVO::getName, BoardStatisticsVO::getCode));

        List<BoardStatisticsVO> list = new ArrayList<>();
        for (AlarmTypeEnum value : AlarmTypeEnum.values()) {
            String code = map.get(String.valueOf(value.getCode()));
            BoardStatisticsVO boardStatisticsVO = new BoardStatisticsVO();
            boardStatisticsVO.setName(value.getName());
            boardStatisticsVO.setCode(code == null ? "0" : code);
            list.add(boardStatisticsVO);
        }
        return list;
    }

    @Override
    public CsdnLiftNumVO getSmartLift(CsdnBoardQuery query) {
        CsdnLiftNumVO csdnLiftNumVO = new CsdnLiftNumVO();
        // 智慧电梯数量
        if (query.getRegisterCodeList().isEmpty()) {
            List<CsdnLiftPositionVO> liftPositions = getLiftPositions(query, true);
            csdnLiftNumVO.setCoverageCount(liftPositions.size());
        } else {
            csdnLiftNumVO.setCoverageCount(query.getRegisterCodeList().size());
        }
        Integer count = csdnBoardMapper.getLiftCount(query);
        csdnLiftNumVO.setTotalCount(count);
        // 试点电梯数量
        if (query.getRegisterCodeList().isEmpty()) {
            List<CsdnLiftPositionVO> liftPositions = getLiftPositions(query, false);
            csdnLiftNumVO.setCoverageCount(liftPositions.size());
        } else {
            csdnLiftNumVO.setCoverageCount(query.getRegisterCodeList().size());
        }
        return csdnLiftNumVO;
    }

    @Override
    public List<CsdnLiftPositionVO> liftLonLat(CsdnBoardQuery query) {
        if (query.getLiftPositType() != null && query.getLiftPositType() == 0) {
            return getLiftPositions(query, false);
        } else if (query.getLiftPositType() != null && query.getLiftPositType() == 1) {
            return getLiftPositions(query, true);
        } else if (query.getLiftPositType() != null && query.getLiftPositType() == 2) {
            // 困人
            return liftAgentRemoteApi.queryAlarmLonLat(query);
        } else if (query.getLiftPositType() != null) {
            // 除了困人类型
            List<CsdnLiftPositionVO> liftLonLatByAlarm = zhptAlarmSiteMapper.getLiftLonLatByAlarm(query);
            for (CsdnLiftPositionVO csdnLiftPositionVO : liftLonLatByAlarm) {
                CsdnBaseResponse<CsdnLift> detail = csdnLiftRemoteApi.detail(csdnLiftPositionVO.getRegisterCode(), false, false);
                CsdnLift csdnLift = detail.getData();
                if (csdnLift != null) {
                    csdnLiftPositionVO.setLat(csdnLift.getLat());
                    csdnLiftPositionVO.setLon(csdnLift.getLon());
                }
            }
            return liftLonLatByAlarm;
        }
        return null;
    }

    @Override
    public CsdnLiftInfoVO alarmLift(CsdnBoardQuery query) {
        return csdnBoardMapper.getLiftInfo(query);
    }

    @Override
    public CsdnAlarmInfoVO getAlarmInfo(CsdnBoardQuery query) {
        CsdnAlarmInfoVO csdnAlarmInfoVO = new CsdnAlarmInfoVO();

        LambdaQueryWrapper<ZhptAlarmSite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ZhptAlarmSite::getRegisterCode, query.getErrorNo());
        queryWrapper.eq(ZhptAlarmSite::getStatus, 0)
                .orderByAsc(ZhptAlarmSite::getCreateTime)
                .last("limit 1");
        ZhptAlarmSite zhptAlarmSite = zhptAlarmSiteMapper.selectOne(queryWrapper);
        if (Objects.nonNull(zhptAlarmSite)) {
            csdnAlarmInfoVO.setName("");
            csdnAlarmInfoVO.setPhone("");
            csdnAlarmInfoVO.setTime(DateUtil.getTimeByDate(zhptAlarmSite.getCreateTime()));
            csdnAlarmInfoVO.setSource("系统接警");
        } else {
            List<DataResponse> dataResponses = liftAgentRemoteApi.getErrorInfo(query.getErrorNo());
            if (dataResponses.isEmpty()) {
                return csdnAlarmInfoVO;
            }
            DataResponse dataResponse = dataResponses.get(0);
            List<DataTransfer> dataTransferList = dataResponse.getData();
            if (dataTransferList.isEmpty()) {
                return csdnAlarmInfoVO;
            }
            for (DataTransfer dataTransfer : dataTransferList) {
                if ("data".equals(dataTransfer.getField())) {
                    LinkedHashMap map = (LinkedHashMap) dataTransfer.getValue();
                    csdnAlarmInfoVO.setName(map.get("errorPerson") == null ? "" : map.get("errorPerson").toString());
                    csdnAlarmInfoVO.setPhone(map.get("phoneNo") == null ? null : map.get("phoneNo").toString());
                    String recordeTime = map.get("recordeTime").toString();
                    if (!StringUtils.isEmpty(recordeTime)) {
                        csdnAlarmInfoVO.setTime(DateUtil.getDateByLong(Long.valueOf(recordeTime)));
                    }
                    csdnAlarmInfoVO.setSource(map.get("errorsourceName") == null ? null : map.get("errorsourceName").toString());
                    csdnAlarmInfoVO.setRemake(map.get("phoneTypeName") == null ? null : map.get("phoneTypeName").toString());
                    csdnAlarmInfoVO.setRegisterCode(map.get("registerCode") == null ? null : map.get("registerCode").toString());
                }
            }
        }
        return csdnAlarmInfoVO;
    }

    @Override
    public CsdnLiftInfoVO getLiftInfo(CsdnBoardQuery query) {
        CsdnBaseResponse<CsdnLift> detail = csdnLiftRemoteApi.detail(query.getRegisterCode(), false, false);
        if (detail == null) {
            throw new BusinessException("该电梯不存在");
        }
        CsdnLift csdnLift = detail.getData();
        CsdnLiftInfoVO csdnLiftInfoVO = new CsdnLiftInfoVO();
        csdnLiftInfoVO.setRegisterCode(csdnLift.getRegisterCode());
        csdnLiftInfoVO.setLocationName(csdnLift.getLocationName());
        csdnLiftInfoVO.setDeviceNumber(csdnLift.getEquipmentCode());
        csdnLiftInfoVO.setLiftName(csdnLift.getInnerNo());
        csdnLiftInfoVO.setDetailAddress(csdnLift.getDetailAddress());
        csdnLiftInfoVO.setMaintainEnterName(csdnLift.getMaintenanceUnitName());
        csdnLiftInfoVO.setUseUnitName(csdnLift.getUseUnitName());
        csdnLiftInfoVO.setLocationType(csdnLift.getLocationType());
        return csdnLiftInfoVO;
    }

    @Override
    public AlarmNoteTimeVO getAlarmNoteTime(CsdnBoardQuery query) {
        AlarmNoteTimeVO alarmNoteTimeVO = new AlarmNoteTimeVO();

        LambdaQueryWrapper<ZhptAlarmSite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ZhptAlarmSite::getRegisterCode, query.getErrorNo());
        queryWrapper.eq(ZhptAlarmSite::getStatus, 0)
                .orderByAsc(ZhptAlarmSite::getCreateTime)
                .last("limit 1");
        ZhptAlarmSite zhptAlarmSite = zhptAlarmSiteMapper.selectOne(queryWrapper);
        if (zhptAlarmSite != null) {
            alarmNoteTimeVO.setErrorNo(query.getErrorNo());
            if (zhptAlarmSite.getNoticeTime() != null) {
                alarmNoteTimeVO.setNotifyTime(DateUtil.getTimeByDate(zhptAlarmSite.getNoticeTime()));
            }
            if (zhptAlarmSite.getReceiveTime() != null) {
                alarmNoteTimeVO.setReceivedTime(DateUtil.getTimeByDate(zhptAlarmSite.getReceiveTime()));
            }
            if (zhptAlarmSite.getEndTime() != null) {
                alarmNoteTimeVO.setEndTime(DateUtil.getTimeByDate(zhptAlarmSite.getEndTime()));
            }
            return alarmNoteTimeVO;
        } else {
            List<DataResponse> dataResponses = liftAgentRemoteApi.getProgress(query.getErrorNo());
            if (dataResponses.isEmpty()) {
                return alarmNoteTimeVO;
            }
            DataResponse dataResponse = dataResponses.get(0);
            List<DataTransfer> dataTransferList = dataResponse.getData();
            if (dataTransferList.isEmpty()) {
                return alarmNoteTimeVO;
            }
            for (DataTransfer dataTransfer : dataTransferList) {
                if ("data".equals(dataTransfer.getField())) {
                    LinkedHashMap map = (LinkedHashMap) dataTransfer.getValue();
                    alarmNoteTimeVO.setErrorNo(map.get("errorNo") == null ? null : map.get("errorNo").toString());
                    alarmNoteTimeVO.setNotifyTime(map.get("noticeTime") == null ? null : map.get("noticeTime").toString());
                    alarmNoteTimeVO.setReceivedTime(map.get("sendTime") == null ? null : map.get("sendTime").toString());
                    alarmNoteTimeVO.setSendTime(map.get("goTime") == null ? null : map.get("goTime").toString());
                    alarmNoteTimeVO.setArriveTime(map.get("arriveTime") == null ? null : map.get("arriveTime").toString());
                    alarmNoteTimeVO.setRescueEndTime(map.get("rescueEndTime") == null ? null : map.get("rescueEndTime").toString());
                    alarmNoteTimeVO.setRepairEndTime(map.get("repairEndTime") == null ? null : map.get("repairEndTime").toString());
                    alarmNoteTimeVO.setEndTime(map.get("endTime") == null ? null : map.get("endTime").toString());
                }
            }
        }
        return alarmNoteTimeVO;
    }

    @SneakyThrows
    @Override
    public WorkerInfoVO getWorkerInfoVO(CsdnBoardQuery query) {
        WorkerInfoVO workerInfoVO = new WorkerInfoVO();

        LambdaQueryWrapper<ZhptAlarmSite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ZhptAlarmSite::getRegisterCode, query.getErrorNo());
        queryWrapper.eq(ZhptAlarmSite::getStatus, 0);
        ZhptAlarmSite zhptAlarmSite = zhptAlarmSiteMapper.selectOne(queryWrapper);
        if (Objects.nonNull(zhptAlarmSite)) {
            return workerInfoVO;
        }

        List<DataResponse> dataResponseList = liftAgentRemoteApi.getRoute(query.getErrorNo());
        List<DataTransfer> dataTransferList = dataResponseList.get(0).getData();
        return processingPoint(workerInfoVO, dataTransferList);
    }

    @Override
    public List<CsdnAlarmVO> queryAlarmInfo(CsdnBoardQuery query) {
        return liftAgentRemoteApi.queryAlarmInfo(query);
    }

    @Override
    public CsdnLiftNumVO queryHinderLiftCount(CsdnBoardQuery query) {
        CsdnLiftNumVO csdnLiftNumVO = new CsdnLiftNumVO();
        // 一年内
        Date currentDate = new Date();
        Date date = DateUtil.addMonths(currentDate, -12);
        query.setStartTime1(DateUtil.getTimeByDate(date));
        query.setEndTime1(DateUtil.getTimeByDate(currentDate));

        Integer useTimesCount = csdnLiftStatisticsService.queryUseTimesCount(query);
        csdnLiftNumVO.setTotalCount(useTimesCount);
        Integer hinderLiftCount = csdnAlarmIncivilizationService.queryHinderLiftCount(query);
        csdnLiftNumVO.setCoverageCount(hinderLiftCount);
        return csdnLiftNumVO;
    }

    @Override
    public List<BoardStatisticsVO> getHinderLiftLocationName(CsdnBoardQuery query) {
        List<String> alarmTypeList = Arrays.stream(IncivilizationTypeEnum.values())
                .map(incivilizationTypeEnum -> String.valueOf(incivilizationTypeEnum.getCode())).collect(Collectors.toList());
        query.setAlarmTypeList(alarmTypeList);
        List<BoardStatisticsVO> boardStatisticsVOS = csdnAlarmIncivilizationService.quryListTop(query);
        int sum = boardStatisticsVOS.stream().mapToInt(boardStatisticsVO -> Integer.parseInt(boardStatisticsVO.getCode())).sum();
        if (sum > 0) {
            for (BoardStatisticsVO boardStatisticsVO : boardStatisticsVOS) {
                BigDecimal code = new BigDecimal(boardStatisticsVO.getCode())
                        .divide(new BigDecimal(sum), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                boardStatisticsVO.setCode(String.valueOf(code));
            }
        }
        return boardStatisticsVOS;
    }

    @Override
    public List<BoardStatisticsVO> getHinderLiftList(CsdnBoardQuery query) {
        Date currentDate = new Date();
        Date firstDayOfMonth = DateUtil.getFirstDayOfMonth(currentDate);
        Date endTimeByToday = DateUtil.getEndTimeByToday(currentDate);
        query.setStartTime1(DateUtil.getTimeByDate(firstDayOfMonth));
        query.setEndTime1(DateUtil.getTimeByDate(endTimeByToday));
        return csdnAlarmIncivilizationService.getHinderLiftList(query);
    }

    @Override
    public List<MalfunctionFromVO> getHinderLiftInfoList(CsdnBoardQuery query) {
        buildTime(query);
        List<MalfunctionFromVO> hinderLiftInfoList = csdnAlarmIncivilizationService.getHinderLiftInfoList(query);
        for (MalfunctionFromVO malfunctionFromVO : hinderLiftInfoList) {
            CsdnBaseResponse<CsdnLift> detail = csdnLiftRemoteApi.detail(malfunctionFromVO.getDeviceNo(), false, false);
            CsdnLift data = detail.getData();
            if (data != null) {
                malfunctionFromVO.setLocationName(data.getLocationName());
                malfunctionFromVO.setLocationType(data.getLocationType());
                malfunctionFromVO.setInnerNo(data.getInnerNo());
                malfunctionFromVO.setMaintenanceUnitName(data.getMaintenanceUnitName());
                malfunctionFromVO.setUseUnitName(data.getUseUnitName());
            }
        }
        return hinderLiftInfoList;
    }

    @Override
    public PublicWelfareVO getPublicWelfare() {
        PublicWelfare publicWelfare = publicWelfareMapper.selectOne(new LambdaQueryWrapper<>());
        PublicWelfareVO publicWelfareVO = new PublicWelfareVO();
        if (publicWelfare == null) {
            publicWelfareVO.setPicUrlList(new ArrayList<>());
            return publicWelfareVO;
        }
        BeanCopyUtils.copyBean(publicWelfare, publicWelfareVO);
        // 字符串图片字段转成数组
        List<String> picUrlList = new ArrayList<>();
        if (publicWelfare.getPicUrls().contains(",")) {
            picUrlList.addAll(Arrays.stream(publicWelfare.getPicUrls().split(",")).collect(Collectors.toList()));
        } else {
            if (!StringUtils.isEmpty(publicWelfare.getPicUrls())) {
                picUrlList.addAll(Collections.singletonList(publicWelfare.getPicUrls()));
            }
        }
        publicWelfareVO.setPicUrlList(picUrlList);
        return publicWelfareVO;
    }

    @Override
    public void updatePublicWelfare(PublicWelfareRequest request) {
        if (!request.getPicUrlList().isEmpty() && request.getPicUrlList().size() > 10) {
            throw new BusinessException("最多只支持十张图片");
        }
        // 不存在添加，多张图片以逗号隔开转成字符串存储
        PublicWelfare publicWelfare = publicWelfareMapper.selectOne(new LambdaQueryWrapper<>());
        if (publicWelfare == null) {
            publicWelfare = new PublicWelfare();
            BeanCopyUtils.copyBean(request, publicWelfare);
            String picUrls = request.getPicUrlList().stream().map(s -> s + ",").collect(Collectors.joining());
            if (!StringUtils.isEmpty(picUrls)) {
                picUrls = picUrls.substring(0, picUrls.length() - 1);
            }
            publicWelfare.setPicUrls(picUrls);
            publicWelfareMapper.insert(publicWelfare);
        } else {
            publicWelfare = new PublicWelfare();
            // 存在修改，id必传
            if (request.getId() == null) {
                throw new BusinessException("数据已存在,请选择修改");
            }
            BeanCopyUtils.copyBean(request, publicWelfare);
            String picUrls = request.getPicUrlList().stream().map(s -> s + ",").collect(Collectors.joining());
            if (!StringUtils.isEmpty(picUrls)) {
                picUrls = picUrls.substring(0, picUrls.length() - 1);
            }
            publicWelfare.setPicUrls(picUrls);
            publicWelfareMapper.updateById(publicWelfare);
        }
    }

    /**
     * 构建查询电梯数量条件对象
     */
    private CsdnLiftQuery buildGetLiftNumQuery(CsdnBoardQuery query) {
        Date currentDate = new Date();
        // 梯领时间段 2000以内需要计算时间段
        CsdnLiftQuery csdnLiftQuery = new CsdnLiftQuery();
        if (Objects.nonNull(query.getLiftTime())) {
            if (query.getLiftTime() < 2000) {
                // 15年以上 时间小于15年
                if (query.getLiftTime() == 16) {
                    Date endTime = DateUtil.addMonths(currentDate, -(query.getLiftTime() - 1));
                    csdnLiftQuery.setEndTime(DateUtil.getTimeByDate(endTime));
                } else {
                    // 其他 大于起始时间 默认0-6年
                    int liftTime = query.getLiftTime() * 12;
                    Date startTime = DateUtil.addMonths(currentDate, -liftTime);
                    csdnLiftQuery.setStartTime(DateUtil.getTimeByDate(startTime));
                    // 6~10年
                    if (query.getLiftTime() == 10) {
                        Date endTime = DateUtil.addMonths(currentDate, -(6 * 12));
                        csdnLiftQuery.setEndTime(DateUtil.getTimeByDate(endTime));
                    } else if (query.getLiftTime() == 15) {
                        // 10~15年
                        Date endTime = DateUtil.addMonths(currentDate, -(10 * 12));
                        csdnLiftQuery.setEndTime(DateUtil.getTimeByDate(endTime));
                    }
                }
            } else {
                String startTime = query.getLiftTime() + "-01-01 00:00:00";
                String endTime = query.getLiftTime() + "-12-31 23:59:59";
                csdnLiftQuery.setStartTime(startTime);
                csdnLiftQuery.setEndTime(endTime);
            }
        }
        if (query.getRegisterCodeList().isEmpty()) {
            csdnLiftQuery.setTownCode(query.getTownCode());
        } else {
            csdnLiftQuery.setRegisterCodeList(query.getRegisterCodeList());
        }
        csdnLiftQuery.setStreetCode(query.getStreetCode());
        csdnLiftQuery.setLocationType(query.getLocationType());
        return csdnLiftQuery;
    }

    /**
     * 获取监管平台电梯信息
     */
    public List<CsdnLiftPositionVO> getLiftPositions(CsdnBoardQuery query, boolean connectNetFlag) {
        List<CsdnLiftPositionVO> csdnLiftPositionVOS = new ArrayList<>();
        CsdnLiftQuery csdnLiftQuery = new CsdnLiftQuery();
        if (query.getRegisterCodeList().isEmpty()) {
            csdnLiftQuery.setTownCode(query.getTownCode());
        } else {
            csdnLiftQuery.setRegisterCodeList(query.getRegisterCodeList());
        }
        if (connectNetFlag) {
            csdnLiftQuery.setConnectNetFlag(1);
        }
        csdnLiftQuery.setLocationType(query.getLocationType());
        csdnLiftQuery.setStartTime(query.getStartTime1());
        csdnLiftQuery.setEndTime(query.getEndTime1());
        csdnLiftQuery.setStreetCode(query.getStreetCode());
        CsdnBaseResponse<List<CsdnLift>> listCsdnBaseResponse = csdnLiftRemoteApi.queryLiftList(csdnLiftQuery);
        if (listCsdnBaseResponse.getData() == null) {
            return csdnLiftPositionVOS;
        }
        List<CsdnLift> csdnLiftList = listCsdnBaseResponse.getData();
        for (CsdnLift csdnLift : csdnLiftList) {
            CsdnLiftPositionVO positionVO = new CsdnLiftPositionVO();
            positionVO.setRegisterCode(csdnLift.getRegisterCode());
            positionVO.setLon(csdnLift.getLon());
            positionVO.setLat(csdnLift.getLat());
            csdnLiftPositionVOS.add(positionVO);
        }
        return csdnLiftPositionVOS;
    }

    /**
     * 计算时间段
     */
    public void buildTime(CsdnBoardQuery query) {
        Date currentDate = new Date();
        if (query.getAlarmFlag() != null && query.getAlarmFlag() == 1) {
            Date firstDayOfYear = DateUtil.getFirstDayOfYear(currentDate);
            Date lastDayOfYear = DateUtil.getLastDayOfYear(currentDate);
            query.setStartTime1(DateUtil.getTimeByDate(firstDayOfYear));
            query.setEndTime1(DateUtil.getTimeByDate(lastDayOfYear));
        } else if (query.getAlarmFlag() != null && query.getAlarmFlag() == 2) {
            Date firstDayOfMonth = DateUtil.getFirstDayOfMonth(currentDate);
            Date lastDayOfMonth = DateUtil.getLastDayOfMonth(currentDate);
            query.setStartTime1(DateUtil.getTimeByDate(firstDayOfMonth));
            query.setEndTime1(DateUtil.getTimeByDate(lastDayOfMonth));
        } else {
            Date startTimeByToday = DateUtil.getStartTimeByToday(currentDate);
            Date endTimeByToday = DateUtil.getEndTimeByToday(currentDate);
            query.setStartTime1(DateUtil.getTimeByDate(startTimeByToday));
            query.setEndTime1(DateUtil.getTimeByDate(endTimeByToday));
        }
    }

    /**
     * 处理杭州看板返回的数据接口
     */
    public WorkerInfoVO processingPoint(WorkerInfoVO workerInfoVO, List<DataTransfer> dataTransferList) {
        for (DataTransfer dataTransfer : dataTransferList) {
            if ("maint".equals(dataTransfer.getField())) {
                List<LinkedHashMap> valueList = (List<LinkedHashMap>) dataTransfer.getValue();
                if (valueList.isEmpty()) {
                    return workerInfoVO;
                }
                LinkedHashMap map = valueList.get(0);
                List<LinkedHashMap> routeMapList = (List<LinkedHashMap>) map.get("routeList");
                LinkedHashMap workerInfoMap = (LinkedHashMap) map.get("workerInfo");

                if (routeMapList != null) {
                    List<Route> routeList = new ArrayList<>();
                    for (LinkedHashMap linkedHashMap : routeMapList) {
                        Route route = new Route();
                        String lon = linkedHashMap.get("lon") == null ? null : linkedHashMap.get("lon").toString();
                        String lat = linkedHashMap.get("lat") == null ? null : linkedHashMap.get("lat").toString();
                        route.setLon(lon);
                        route.setLat(lat);
                        routeList.add(route);
                    }
                    workerInfoVO.setRouteList(routeList);
                }
                if (workerInfoMap != null) {
                    workerInfoVO.setName(workerInfoMap.get("name") == null ? "" : workerInfoMap.get("name").toString());
                    workerInfoVO.setPhone(workerInfoMap.get("phone") == null ? null : workerInfoMap.get("phone").toString());
                }
            } else if ("endPoint".equals(dataTransfer.getField())) {
                LinkedHashMap map = (LinkedHashMap) dataTransfer.getValue();
                Route route = new Route();
                route.setLon(map.get("lon") == null ? null : map.get("lon").toString());
                route.setLat(map.get("lat") == null ? null : map.get("lat").toString());
                workerInfoVO.setEndPoint(route);
            }
        }
        return workerInfoVO;
    }

    /**
     * 补充月度中没有数据的一天
     */
    public List<BoardStatisticsVO> buildDateList(List<BoardStatisticsVO> malfunctionMonth, Date currentDate) {
        Map<String, BoardStatisticsVO> malfunctionMonthMap = malfunctionMonth.stream()
                .collect(Collectors.toMap(BoardStatisticsVO::getName, Function.identity()));

        List<BoardStatisticsVO> list = new ArrayList<>();
        int day = DateUtil.getDayOfMonth(currentDate);
        Integer month = DateUtil.getMonth(currentDate);
        String months = month < 10 ? "0" + month : month + "";
        Integer year = DateUtil.getYear(currentDate);
        for (int i = 1; i <= day; i++) {
            String days = i < 10 ? "0" + i : i + "";
            String dates = year + "-" + months + "-" + days;

            BoardStatisticsVO boardStatisticsVO = malfunctionMonthMap.get(dates);
            if (boardStatisticsVO != null) {
                String name = boardStatisticsVO.getName();
                String[] split = name.split("-");
                name = Integer.parseInt(split[split.length - 1]) + "日";
                boardStatisticsVO.setName(name);
                list.add(boardStatisticsVO);
            } else {
                BoardStatisticsVO boardStatisticsVO1 = new BoardStatisticsVO();
                boardStatisticsVO1.setName(i + "日");
                boardStatisticsVO1.setCode("0");
                list.add(boardStatisticsVO1);
            }
        }
        return list;
    }

    /**
     * 初始化告警服务token
     */
    public void initLiftAgentToken(String townCode) {
        LoginParam loginParam = new LoginParam();
        loginParam.setUsername(townCode);
        loginParam.setPassword("0c1c7054442c41168a2f77f3718a023b");
        LoginResponse response = liftAgentRemoteApi.doLogin(loginParam).getData();
        redisTokenCacheService.put("LIFT_AGENT_TOKEN", response.getToken());
    }
}
