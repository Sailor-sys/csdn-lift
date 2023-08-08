package com.xzl.csdn.service;

import com.xzl.csdn.domain.entity.CsdnLiftStatistics;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.query.CsdnLiftQuery;
import com.xzl.csdn.domain.vo.BoardStatisticsVO;
import com.xzl.csdn.domain.vo.MalfunctionFromVO;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-25 14:23
 **/
public interface CsdnLiftStatisticsService {

    Integer queryCount(CsdnLiftQuery csdnLiftQuery);

    void insert(CsdnLiftStatistics csdnLiftStatistics);

    /**
     * 根据时间根据获取各个月份数据
     */
    List<BoardStatisticsVO> selectListStatistics(CsdnBoardQuery query);

    /**
     * 获取运行里程数据
     */
    List<MalfunctionFromVO> getRunInfoList(CsdnBoardQuery query);

    /**
     * 获取覆盖人次
     */
    Integer queryUseTimesCount(CsdnBoardQuery query);

    /**
     * 查询同步数据
     */
    Integer querySyncCount(CsdnLiftQuery query);
}
