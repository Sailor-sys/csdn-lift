package com.xzl.csdn.service;

import com.xzl.csdn.domain.entity.CsdnAlarmIncivilization;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.vo.BoardStatisticsVO;
import com.xzl.csdn.domain.vo.CsdnLiftPositionVO;
import com.xzl.csdn.domain.vo.MalfunctionFromVO;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-25 17:48
 **/
public interface CsdnAlarmIncivilizationService {

    Integer getAlarmCount(CsdnBoardQuery query);

    /**
     * 添加
     */
    void insert(CsdnAlarmIncivilization csdnAlarmIncivilization);

    /**
     * 获取数量
     */
    Integer queryCount(CsdnBoardQuery query);

    /**
     * 获取阻梯Top5
     */
    List<BoardStatisticsVO> quryListTop(CsdnBoardQuery query);

    /**
     * 阻梯总数
     */
    Integer queryHinderLiftCount(CsdnBoardQuery query);

    /**
     * 获取月度电梯阻梯列表
     */
    List<BoardStatisticsVO> getHinderLiftList(CsdnBoardQuery query);

    /**
     * 查询各个类型统计
     */
    List<BoardStatisticsVO> queryListByType(CsdnBoardQuery query);

    /**
     * 查询阻梯top10
     */
    List<MalfunctionFromVO> getHinderLiftInfoList(CsdnBoardQuery query);

}
