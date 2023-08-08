package com.xzl.csdn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzl.csdn.domain.entity.WxbUser;
import com.xzl.csdn.domain.query.WxbErrorTrack;
import com.xzl.csdn.domain.vo.AlarmDetailVO;

/**
 * @author shiqh
 * @date 2023-07-28 10:09
 **/
@DS("resuce")
public interface WxbErrorTrackMapper extends BaseMapper<WxbErrorTrack> {

    /**
     * 获取用户信息
     */
    WxbUser getUserById(Integer userId);

    /**
     * 获取告警详情
     */
    AlarmDetailVO getAlarmDetailInfo(String errorNo);
}
