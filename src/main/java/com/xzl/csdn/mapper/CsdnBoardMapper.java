package com.xzl.csdn.mapper;

import com.xzl.csdn.domain.entity.ZhptLiftInfo;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-19 13:47
 **/
public interface CsdnBoardMapper {

    /**
     * 获取电梯数量
     */
    Integer getLiftCount(@Param("query") CsdnBoardQuery query);

    /**
     * 获取电梯信息
     */
    CsdnLiftInfoVO getLiftInfo(@Param("query") CsdnBoardQuery query);

    /**
     * 查询指挥电梯信息
     */
    List<ZhptLiftInfo> queryLiftInfo(@Param("query") CsdnBoardQuery query);

}
