package com.xzl.csdn.service.impl;

import com.xzl.csdn.domain.entity.CsdnLift;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.query.CsdnLiftQuery;
import com.xzl.csdn.domain.vo.CsdnLiftInfoVO;
import com.xzl.csdn.domain.vo.CsdnLiftPositionVO;
import com.xzl.csdn.mapper.CsdnLiftMapper;
import com.xzl.csdn.service.CsdnLiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-18 16:16
 **/
@Service
public class CsdnLiftServiceImpl implements CsdnLiftService {

    @Autowired
    private CsdnLiftMapper csdnLiftMapper;

    @Override
    public List<String> queryListByLocationName(CsdnBoardQuery query) {
        return csdnLiftMapper.queryListByLocationName(query);
    }

    @Override
    public CsdnLiftPositionVO getLiftLonLat(CsdnBoardQuery query) {
        return csdnLiftMapper.getLiftLonLat(query);
    }

    @Override
    public CsdnLift getLift(String registerCode) {
        return csdnLiftMapper.getLift(registerCode);
    }

    @Override
    public List<CsdnLift> queryLift(CsdnLiftQuery query) {
        return csdnLiftMapper.queryLift(query);
    }
}
