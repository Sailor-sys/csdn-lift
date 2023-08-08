package com.xzl.csdn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzl.csdn.domain.entity.ZhptAlarmSite;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.vo.CsdnLiftPositionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-28 14:03
 **/
public interface ZhptAlarmSiteMapper extends BaseMapper<ZhptAlarmSite> {

    /**
     * 查询不文明告警点位
     */
    List<CsdnLiftPositionVO> getLiftLonLatByAlarm(@Param("query") CsdnBoardQuery query);
}
