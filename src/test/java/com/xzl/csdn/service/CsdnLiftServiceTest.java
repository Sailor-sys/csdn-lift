package com.xzl.csdn.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xzl.csdn.domain.entity.CsdnLift;
import com.xzl.csdn.domain.entity.ZhptAlarmSite;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.mapper.ZhptAlarmSiteMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CsdnLiftServiceTest {

    @Autowired
    private CsdnLiftService csdnLiftService;
    @Autowired
    private CsdnBoardService csdnBoardService;
    @Autowired
    private ZhptAlarmSiteMapper zhptAlarmSiteMapper;

    @Test
    public void getLift() {
        CsdnLift lift = csdnLiftService.getLift("30103301832004120012");
        System.out.println(lift);
    }

    @Test
    public void getAlarmInfo() {
        LambdaQueryWrapper<ZhptAlarmSite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ZhptAlarmSite::getRegisterCode, "30103301832004120012");
        queryWrapper.eq(ZhptAlarmSite::getStatus, 0)
                .orderByAsc(ZhptAlarmSite::getCreateTime)
                .last("limit 1");
        ZhptAlarmSite zhptAlarmSite = zhptAlarmSiteMapper.selectOne(queryWrapper);
        System.out.println(zhptAlarmSite);
    }
}