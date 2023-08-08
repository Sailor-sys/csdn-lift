package com.xzl.csdn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzl.csdn.domain.entity.CsdnLiftStatistics;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.query.CsdnLiftQuery;
import com.xzl.csdn.domain.vo.BoardStatisticsVO;
import com.xzl.csdn.domain.vo.MalfunctionFromVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-25 14:25
 **/
public interface CsdnLiftStatisticsMapper extends BaseMapper<CsdnLiftStatistics> {

    List<BoardStatisticsVO> selectListStatistics(@Param("query") CsdnBoardQuery query);

    /**
     * 查询运行里程TOP10
     */
    List<MalfunctionFromVO> getRunInfoList(@Param("query") CsdnBoardQuery query);

    /**
     * 查询覆盖人次
     */
    Integer queryUseTimesCount(@Param("query") CsdnBoardQuery query);

    /**
     * 查询统计数量
     */
    Integer queryCount(@Param("query") CsdnLiftQuery query);

    /**
     * 查询同步数量
     */
    Integer querySyncCount(@Param("query") CsdnLiftQuery query);
}

