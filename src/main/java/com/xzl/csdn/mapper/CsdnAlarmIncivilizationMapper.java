package com.xzl.csdn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzl.csdn.domain.entity.CsdnAlarmIncivilization;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.vo.BoardStatisticsVO;
import com.xzl.csdn.domain.vo.CsdnLiftPositionVO;
import com.xzl.csdn.domain.vo.MalfunctionFromVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-25 17:49
 **/
public interface CsdnAlarmIncivilizationMapper extends BaseMapper<CsdnAlarmIncivilization> {


    Integer getAlarmCount(@Param("query") CsdnBoardQuery query);

    /**
     * 获取阻梯TOP5
     */
    List<BoardStatisticsVO> quryListTop(@Param("query") CsdnBoardQuery query);

    /**
     * 阻梯总数
     */
    Integer queryHinderLiftCount(@Param("query") CsdnBoardQuery query);

    /**
     * 获取月度电梯阻梯列表
     */
    List<BoardStatisticsVO> getHinderLiftList(@Param("query") CsdnBoardQuery query);

    /**
     * 查询各个类型统计
     */
    List<BoardStatisticsVO> queryListByType(@Param("query") CsdnBoardQuery query);

    /**
     * 查询阻梯TOP10
     */
    List<MalfunctionFromVO> getHinderLiftInfoList(@Param("query") CsdnBoardQuery query);

}
