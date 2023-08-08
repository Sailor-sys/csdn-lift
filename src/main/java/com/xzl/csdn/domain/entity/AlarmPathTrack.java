package com.xzl.csdn.domain.entity;

import com.xzl.csdn.domain.vo.Route;
import lombok.Data;

import java.util.List;

/**
 * @author gll
 * 2020/1/9 15:09
 */
@Data
public class AlarmPathTrack {

    private List<Route> routeList;

    private AlarmPathExecutor workerInfo;
}
