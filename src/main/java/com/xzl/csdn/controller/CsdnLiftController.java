package com.xzl.csdn.controller;

import com.xzl.csdn.domain.entity.CsdnLift;
import com.xzl.csdn.remote.CsdnLiftRemoteApi;
import com.xzl.csdn.service.CsdnLiftService;
import com.xzl.csdn.support.RestApi;
import com.xzl.zq.csdn.response.CsdnBaseResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author shiqh
 * @date 2023-07-18 16:15
 **/
@RestApi("/csdn/lift")
@Api(tags = "电梯接口")
public class CsdnLiftController {

    @Autowired
    private CsdnLiftService csdnLiftService;

    @Autowired
    private CsdnLiftRemoteApi csdnLiftRemoteApi;

    @GetMapping("/selectLift")
    public void selectLift(String registerCode) {
        CsdnBaseResponse<CsdnLift> detail = csdnLiftRemoteApi.detail(registerCode, false, false);
        System.out.println(detail);
    }

}
