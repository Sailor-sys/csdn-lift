package com.xzl.csdn.service;

import com.xzl.csdn.domain.entity.CsdnLift;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.query.CsdnLiftQuery;
import com.xzl.csdn.domain.vo.CsdnLiftInfoVO;
import com.xzl.csdn.domain.vo.CsdnLiftPositionVO;

import java.util.List;

public interface CsdnLiftService {

    /**
     * 根据场所名称查找电梯
     */
    List<String> queryListByLocationName(CsdnBoardQuery query);


    /**
     * 查询电梯点位
     */
    CsdnLiftPositionVO getLiftLonLat(CsdnBoardQuery query);

    /**
     * 查询电梯
     */
    CsdnLift getLift(String registerCode);

    /**
     * 查询电梯
     */
    List<CsdnLift> queryLift(CsdnLiftQuery query);

}
