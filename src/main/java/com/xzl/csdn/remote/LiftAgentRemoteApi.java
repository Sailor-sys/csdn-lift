package com.xzl.csdn.remote;

import com.xzl.csdn.config.DefaultHttpsConfig;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.domain.vo.BoardStatisticsVO;
import com.xzl.csdn.domain.vo.CsdnAlarmVO;
import com.xzl.csdn.domain.vo.CsdnLiftPositionVO;
import com.xzl.csdn.domain.vo.RescueNumStatisticsVO;
import com.xzl.csdn.remote.request.LoginParam;
import com.xzl.csdn.remote.response.AgentBaseResponse;
import com.xzl.csdn.remote.response.AuthVideoResponse;
import com.xzl.csdn.remote.response.DataResponse;
import com.xzl.csdn.remote.response.LoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author gll
 * 2019/12/24 17:11
 */
@FeignClient(url = "${lift.agent.url}", name = "liftAgentRemoteApi", configuration = DefaultHttpsConfig.class)
public interface LiftAgentRemoteApi {

    /**
     * 登陆
     */
    @PostMapping("/auth/doLogin")
    AgentBaseResponse<LoginResponse> doLogin(@RequestBody LoginParam loginParam);

    /**
     * 实时视频
     */
    @GetMapping("/api/v1/getAlarmVideo")
    List<DataResponse> getAlarmVideo(@RequestParam("registerCode") String registerCode);

    /**
     * 旧视频
     */
    @GetMapping("/api/v1/getOldAlarmVideo")
    List<DataResponse> getOldAlarmVideo(@RequestParam("errorNo") String errorNo);

    /**
     * 告警统计
     */
    @PostMapping("/api/v1/errorinfo/getRescueNumStatistics")
    RescueNumStatisticsVO getRescueNumStatistics(@RequestBody CsdnBoardQuery query);

    /**
     * 告警月度统计
     */
    @PostMapping("/api/v1/errorinfo/getMalfunctionMonth")
    List<BoardStatisticsVO> getMalfunctionMonth(@RequestBody CsdnBoardQuery query);


    /**
     * 最新二十条告警信息
     */
    @PostMapping("/api/v1/errorinfo/queryAlarmInfo")
    List<CsdnAlarmVO> queryAlarmInfo(@RequestBody CsdnBoardQuery query);

    /**
     * 告警信息
     */
    @GetMapping("/api/v1/zhpt/getErrorInfo")
    List<DataResponse> getErrorInfo(@RequestParam("errorNo") String errorNo);

    /**
     * 告警节点
     */
    @GetMapping("/api/v1/zhpt/getProgress")
    List<DataResponse> getProgress(@RequestParam("errorNo") String errorNo);

    /**
     * 告警救援轨迹
     */
    @GetMapping("/api/v1/zhpt/getRoute")
    List<DataResponse> getRoute(@RequestParam("errorNo") String errorNo);

    /**
     * 告警点位
     */
    @PostMapping("/api/v1/errorinfo/queryAlarmLonLat")
    List<CsdnLiftPositionVO> queryAlarmLonLat(@RequestBody CsdnBoardQuery request);

}
