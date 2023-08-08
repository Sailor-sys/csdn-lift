package com.xzl.csdn.controller;

import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.query.PublicWelfareRequest;
import com.xzl.csdn.domain.vo.*;
import com.xzl.csdn.remote.LiftAgentRemoteApi;
import com.xzl.csdn.remote.response.DataTransfer;
import com.xzl.csdn.service.CsdnBoardService;
import com.xzl.csdn.support.RestApi;
import com.xzl.csdn.task.CsdnLiftInfoTask;
import com.xzl.csdn.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-19 11:15
 **/
@Api(tags = "看板接口")
@RestController
@RestApi("/csdn/board")
public class CsdnBoardController {

    @Autowired
    private CsdnBoardService csdnBoardService;
    @Autowired
    private CsdnLiftInfoTask csdnLiftInfoTask;
    @Autowired
    private LiftAgentRemoteApi liftAgentRemoteApi;

    @ApiOperation("同步运行里程/覆盖人次统计数据")
    @GetMapping("/syncLiftStatistics")
    public void syncLiftStatistics(@ApiParam("1：运行里程 2:覆盖人次") Integer type) {
        csdnLiftInfoTask.syncLiftStatistics(type);
    }

    @ApiOperation("同步不文明告警")
    @GetMapping("/syncIncivilizationType")
    public void syncIncivilizationType() {
        csdnLiftInfoTask.syncIncivilizationType();
    }

    @ApiOperation("同步告警数据")
    @GetMapping("/syncAlarmStatistics")
    public void syncAlarmStatistics() {
        Date currentDate = new Date();
        Date firstDayOfMonth = DateUtil.getFirstDayOfMonth(currentDate);
        Date lastDayOfMonth = DateUtil.getLastDayOfMonth(currentDate);
        csdnLiftInfoTask.syncAlarmStatistics(firstDayOfMonth, lastDayOfMonth);
    }

    @ApiOperation("应急救援数量统计")
    @GetMapping("/getRescueNum")
    public RescueNumStatisticsVO getRescueNumStatistics(CsdnBoardQuery query) {
        return csdnBoardService.getRescueNumStatistics(query);
    }

    @ApiOperation("应急救援月度统计")
    @GetMapping("/getMalfunctionMonth")
    public List<BoardStatisticsVO> getMalfunctionMonth(CsdnBoardQuery query) {
        return csdnBoardService.getMalfunctionMonth(query);
    }

    @ApiOperation("试点月度统计")
    @GetMapping("/getPilotMonth")
    public PilotMonthVO getPilotMonth(CsdnBoardQuery query) {
        return csdnBoardService.getPilotMonth(query);
    }

    @ApiOperation("故障分析")
    @GetMapping("/getMalfunction")
    public MalfunctionRateVO getMalfunctionStatistics(CsdnBoardQuery query) {
        return csdnBoardService.getMalfunctionStatistics(query);
    }

    @ApiOperation("故障分析-表单")
    @GetMapping("/form/getMalfunction")
    public List<MalfunctionFromVO> getMalfunctionFrom(CsdnBoardQuery query) {
        return csdnBoardService.getMalfunctionFrom(query);
    }

    @ApiOperation("试点电梯")
    @GetMapping("/getPilotLift")
    public PilotLiftVO getPilotLift(CsdnBoardQuery query) {
        return csdnBoardService.getPilotLift(query);
    }

    @ApiOperation("电梯运行里程-表单")
    @GetMapping("/form/getRunInfo")
    public List<MalfunctionFromVO> getRunInfo(CsdnBoardQuery query) {
        return csdnBoardService.getRunInfo(query);
    }

    @ApiOperation("基本电梯数量")
    @GetMapping("/getLiftNum")
    public CsdnLiftNumVO getLiftNum(CsdnBoardQuery query) {
        return csdnBoardService.getLiftNum(query);
    }

    @ApiOperation("实时概况")
    @GetMapping("/getRealTimeAlarm")
    public List<BoardStatisticsVO> getRealTimeAlarm(CsdnBoardQuery query) {
        return csdnBoardService.getRealTimeAlarm(query);
    }

    @ApiOperation("智慧电梯")
    @GetMapping("/getSmartLift")
    public CsdnLiftNumVO getSmartLift(CsdnBoardQuery query) {
        return csdnBoardService.getSmartLift(query);
    }

    @ApiOperation("电梯点位")
    @GetMapping("/liftLonLat")
    public List<CsdnLiftPositionVO> liftLonLat(CsdnBoardQuery query) {
        return csdnBoardService.liftLonLat(query);
    }

    @ApiOperation("告警电梯信息")
    @GetMapping("/alarmLift")
    public CsdnLiftInfoVO alarmLift(CsdnBoardQuery query) {
        return csdnBoardService.alarmLift(query);
    }

    @ApiOperation("告警信息")
    @GetMapping("/getAlarmInfo")
    public CsdnAlarmInfoVO getAlarmInfo(CsdnBoardQuery query) {
        return csdnBoardService.getAlarmInfo(query);
    }

    @ApiOperation("电梯信息")
    @GetMapping("/getLiftInfo")
    public CsdnLiftInfoVO getLiftInfo(CsdnBoardQuery query) {
        return csdnBoardService.getLiftInfo(query);
    }

    @ApiOperation("流程节点时间")
    @GetMapping("/getAlarmNoteTime")
    public AlarmNoteTimeVO getAlarmNoteTime(CsdnBoardQuery query) {
        return csdnBoardService.getAlarmNoteTime(query);
    }

    @ApiOperation("救援人员信息")
    @GetMapping("/getWorkerInfoVO")
    public WorkerInfoVO getWorkerInfoVO(CsdnBoardQuery query) {
        return csdnBoardService.getWorkerInfoVO(query);
    }

    @ApiOperation("地图下方-列表")
    @GetMapping("/queryAlarmInfo")
    public List<CsdnAlarmVO> queryAlarmInfo(CsdnBoardQuery query) {
        return csdnBoardService.queryAlarmInfo(query);
    }

    @ApiOperation("阻梯统计")
    @GetMapping("/queryHinderLiftCount")
    public CsdnLiftNumVO queryHinderLiftCount(CsdnBoardQuery query) {
        return csdnBoardService.queryHinderLiftCount(query);
    }

    @ApiOperation("月度电梯阻梯统计")
    @GetMapping("/getHinderLiftLocationName")
    public List<BoardStatisticsVO> getHinderLiftLocationName(CsdnBoardQuery query) {
        return csdnBoardService.getHinderLiftLocationName(query);
    }

    @ApiOperation("月度电梯阻梯列表")
    @GetMapping("/getHinderLiftList")
    public List<BoardStatisticsVO> getHinderLiftList(CsdnBoardQuery query) {
        return csdnBoardService.getHinderLiftList(query);
    }

    @ApiOperation("月度电梯阻梯表单")
    @GetMapping("/form/getHinderLiftList")
    public List<MalfunctionFromVO> getHinderLiftInfoList(CsdnBoardQuery query) {
        return csdnBoardService.getHinderLiftInfoList(query);
    }

    @ApiOperation("获取公益宣传")
    @GetMapping("/getPublicWelfare")
    public PublicWelfareVO getPublicWelfare() {
        return csdnBoardService.getPublicWelfare();
    }

    @ApiOperation("修改公益宣传")
    @PostMapping("/updatePublicWelfare")
    public void updatePublicWelfare(@RequestBody PublicWelfareRequest request) {
        csdnBoardService.updatePublicWelfare(request);
    }

    @ApiOperation("实时视频")
    @GetMapping(value = "/getAlarmVideo")
    public List<DataTransfer> getAlarmVideo(String registerCode) {
        return liftAgentRemoteApi.getAlarmVideo(registerCode).get(0).getData();
    }

    @ApiOperation("历史视频")
    @GetMapping(value = "/getOldAlarmVideo")
    public List<DataTransfer> get(String errorNo) {
        return liftAgentRemoteApi.getOldAlarmVideo(errorNo).get(0).getData();
    }
}
