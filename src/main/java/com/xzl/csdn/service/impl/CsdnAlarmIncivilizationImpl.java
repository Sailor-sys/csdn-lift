package com.xzl.csdn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xzl.csdn.domain.entity.CsdnAlarmIncivilization;
import com.xzl.csdn.domain.entity.CsdnLiftStatistics;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.vo.BoardStatisticsVO;
import com.xzl.csdn.domain.vo.CsdnLiftPositionVO;
import com.xzl.csdn.domain.vo.MalfunctionFromVO;
import com.xzl.csdn.mapper.CsdnAlarmIncivilizationMapper;
import com.xzl.csdn.service.CsdnAlarmIncivilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author shiqh
 * @date 2023-07-25 17:48
 **/
@Service
public class CsdnAlarmIncivilizationImpl implements CsdnAlarmIncivilizationService {

    @Autowired
    private CsdnAlarmIncivilizationMapper csdnAlarmIncivilizationMapper;

    @Override
    public Integer getAlarmCount(CsdnBoardQuery query) {
        return csdnAlarmIncivilizationMapper.getAlarmCount(query);
    }


    @Override
    public void insert(CsdnAlarmIncivilization csdnAlarmIncivilization) {
        csdnAlarmIncivilizationMapper.insert(csdnAlarmIncivilization);
    }

    @Override
    public Integer queryCount(CsdnBoardQuery query) {
        LambdaQueryWrapper<CsdnAlarmIncivilization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(CsdnAlarmIncivilization::getCreateTime, query.getStartTime1())
                .le(CsdnAlarmIncivilization::getCreateTime, query.getEndTime1());
        return csdnAlarmIncivilizationMapper.selectCount(queryWrapper);
    }

    @Override
    public List<BoardStatisticsVO> quryListTop(CsdnBoardQuery query) {
        return csdnAlarmIncivilizationMapper.quryListTop(query);
    }

    @Override
    public Integer queryHinderLiftCount(CsdnBoardQuery query) {
        return csdnAlarmIncivilizationMapper.queryHinderLiftCount(query);
    }

    @Override
    public List<BoardStatisticsVO> getHinderLiftList(CsdnBoardQuery query) {
        return csdnAlarmIncivilizationMapper.getHinderLiftList(query);
    }

    @Override
    public List<BoardStatisticsVO> queryListByType(CsdnBoardQuery query) {
        return csdnAlarmIncivilizationMapper.queryListByType(query);
    }

    @Override
    public List<MalfunctionFromVO> getHinderLiftInfoList(CsdnBoardQuery query) {
        return csdnAlarmIncivilizationMapper.getHinderLiftInfoList(query);
    }

}
