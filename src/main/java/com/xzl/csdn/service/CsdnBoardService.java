package com.xzl.csdn.service;

import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.query.PublicWelfareRequest;
import com.xzl.csdn.domain.vo.*;

import java.util.List;

public interface CsdnBoardService {

    /**
     * 应急救援故障数量信息统计
     */
    RescueNumStatisticsVO getRescueNumStatistics(CsdnBoardQuery query);

    /**
     * 应急救援月度困人统计
     */
    List<BoardStatisticsVO> getMalfunctionMonth(CsdnBoardQuery query);

    /**
     * 试点月度统计
     */
    PilotMonthVO getPilotMonth(CsdnBoardQuery query);

    /**
     * 获取更类型故障占比
     */
    MalfunctionRateVO getMalfunctionStatistics(CsdnBoardQuery query);

    /**
     * 故障类型表单
     */
    List<MalfunctionFromVO> getMalfunctionFrom(CsdnBoardQuery query);

    /**
     * 年累计数据统计
     */
    PilotLiftVO getPilotLift(CsdnBoardQuery query);

    /**
     * 月度运行里程和月度覆盖人次
     */
    List<MalfunctionFromVO> getRunInfo(CsdnBoardQuery query);

    /**
     * 获取电梯数量
     */
    CsdnLiftNumVO getLiftNum(CsdnBoardQuery query);

    /**
     * 查询电梯各类型故障统计
     */
    List<BoardStatisticsVO> getRealTimeAlarm(CsdnBoardQuery query);

    /**
     * 获取智慧电梯统计数据
     */
    CsdnLiftNumVO getSmartLift(CsdnBoardQuery query);

    /**
     * 查询电梯点位
     */
    List<CsdnLiftPositionVO> liftLonLat(CsdnBoardQuery query);

    /**
     * 告警电梯信息
     */
    CsdnLiftInfoVO alarmLift(CsdnBoardQuery query);

    /**
     * 获取告警信息
     */
    CsdnAlarmInfoVO getAlarmInfo(CsdnBoardQuery query);

    /**
     * 获取电梯信息
     */
    CsdnLiftInfoVO getLiftInfo(CsdnBoardQuery query);

    /**
     * 救援节点时间
     */
    AlarmNoteTimeVO getAlarmNoteTime(CsdnBoardQuery query);

    /**
     * 获取救援人员信息
     */
    WorkerInfoVO getWorkerInfoVO(CsdnBoardQuery query);

    /**
     * 最近二十条告警信息
     */
    List<CsdnAlarmVO> queryAlarmInfo(CsdnBoardQuery query);

    /**
     * 查询阻梯统计
     */
    CsdnLiftNumVO queryHinderLiftCount(CsdnBoardQuery query);

    /**
     * 获取月度电梯阻梯
     */
    List<BoardStatisticsVO> getHinderLiftLocationName(CsdnBoardQuery query);

    /**
     * 获取月度电梯阻梯列表
     */
    List<BoardStatisticsVO> getHinderLiftList(CsdnBoardQuery query);

    /**
     * 获取月度阻梯表单
     */
    List<MalfunctionFromVO> getHinderLiftInfoList(CsdnBoardQuery query);

    /**
     * 获取公益宣传信息
     */
    PublicWelfareVO getPublicWelfare();

    /**
     * 更新公益宣传
     */
    void updatePublicWelfare(PublicWelfareRequest request);

}
