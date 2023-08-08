package com.xzl.csdn.mapper;

import com.xzl.csdn.domain.entity.CsdnLift;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.query.CsdnLiftQuery;
import com.xzl.csdn.domain.vo.CsdnLiftInfoVO;
import com.xzl.csdn.domain.vo.CsdnLiftPositionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-21 13:53
 **/
public interface CsdnLiftMapper {

    /**
     * 根据场所名称查询电梯
     */
    List<String> queryListByLocationName(@Param("query") CsdnBoardQuery query);

    /**
     * 电梯点位
     */
    CsdnLiftPositionVO getLiftLonLat(@Param("query") CsdnBoardQuery query);

    /**
     * 查询电梯
     */
    CsdnLift getLift(String registerCode);

    /**
     * 获取电梯
     */
    List<CsdnLift> queryLift(@Param("query") CsdnLiftQuery query);
}
