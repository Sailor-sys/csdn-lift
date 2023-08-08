package com.xzl.csdn.remote;

import com.xzl.csdn.config.DataCenterFeignConfig;
import com.xzl.csdn.domain.entity.CsdnLift;
import com.xzl.csdn.domain.entity.CsdnUser;
import com.xzl.csdn.domain.query.CsdnLiftQuery;
import com.xzl.zq.csdn.response.CsdnBaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author gll
 * 2019/9/7 15:54
 */
@FeignClient(url="${csdn.server.ip}" ,path = "/datacenter/api/v1/" ,name ="data-center-lift",configuration = DataCenterFeignConfig.class)
public interface CsdnLiftRemoteApi {


    /**
     * 电梯详情
     */
    @GetMapping("/liftInfo/detail")
    CsdnBaseResponse<CsdnLift> detail(@RequestParam("registerCode") String registerCode,
                                      @RequestParam("maintenanceOverdualFlag") Boolean maintenanceOverdualFlag,
                                      @RequestParam("checkOverdualFlag")Boolean checkOverdualFlag);

    /**
     * 查询电梯
     */
    @PostMapping("/liftInfo/queryLiftList")
    CsdnBaseResponse<List<CsdnLift>> queryLiftList(@RequestBody CsdnLiftQuery query);

    /**
     * 查询用户
     */
    @GetMapping("/liftInfo/queryUser")
    CsdnBaseResponse<CsdnUser> queryUser(@RequestParam("username") String username,@RequestParam("id") Integer id);

}

