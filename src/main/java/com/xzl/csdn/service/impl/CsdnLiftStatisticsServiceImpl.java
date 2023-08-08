package com.xzl.csdn.service.impl;

import com.xzl.csdn.domain.entity.CsdnLiftStatistics;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.query.CsdnLiftQuery;
import com.xzl.csdn.domain.vo.BoardStatisticsVO;
import com.xzl.csdn.domain.vo.MalfunctionFromVO;
import com.xzl.csdn.mapper.CsdnLiftStatisticsMapper;
import com.xzl.csdn.service.CsdnLiftStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-25 14:24
 **/
@Service
public class CsdnLiftStatisticsServiceImpl implements CsdnLiftStatisticsService {

    @Autowired
    private CsdnLiftStatisticsMapper csdnLiftStatisticsMapper;

    @Override
    public Integer queryCount(CsdnLiftQuery query) {
        return csdnLiftStatisticsMapper.queryCount(query);
    }

    @Override
    public void insert(CsdnLiftStatistics csdnLiftStatistics) {
        csdnLiftStatisticsMapper.insert(csdnLiftStatistics);
    }

    @Override
    public List<BoardStatisticsVO> selectListStatistics(CsdnBoardQuery query) {
        return csdnLiftStatisticsMapper.selectListStatistics(query);
    }

    @Override
    public List<MalfunctionFromVO> getRunInfoList(CsdnBoardQuery query) {
        return csdnLiftStatisticsMapper.getRunInfoList(query);
    }

    @Override
    public Integer queryUseTimesCount(CsdnBoardQuery query) {
        return csdnLiftStatisticsMapper.queryUseTimesCount(query);
    }

    @Override
    public Integer querySyncCount(CsdnLiftQuery query) {
        return csdnLiftStatisticsMapper.querySyncCount(query);
    }
}
