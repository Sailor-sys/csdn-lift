package com.xzl.csdn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzl.csdn.domain.entity.CsdnAlarmStatistics;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.vo.BoardStatisticsVO;
import com.xzl.csdn.domain.vo.MalfunctionFromVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-08-03 16:00
 * @desc 告警类型
 **/
public interface CsdnAlarmStatisticsMapper extends BaseMapper<CsdnAlarmStatistics> {

    /**
     * 告警类型统计
     */
    List<BoardStatisticsVO> getAlarmStatistics(@Param("query") CsdnBoardQuery query);

    /**
     * 查询列表
     */
    List<MalfunctionFromVO> queryAlarmStatisticsList(@Param("query") CsdnBoardQuery query);

    /**
     * 月度告警统计
     */
    List<BoardStatisticsVO> getAlarmStatisticsMonth(@Param("query") CsdnBoardQuery query);
}
