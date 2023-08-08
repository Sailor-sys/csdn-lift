package com.xzl.csdn.remote;

import com.xzl.csdn.common.http.request.DataCenterTokenRequest;
import com.xzl.csdn.common.http.response.DataCenterTokenResponse;
import com.xzl.csdn.config.DataCenterFeignTokenConfig;
import com.xzl.zq.csdn.response.CsdnBaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author gll
 * 2019/8/19 14:33
 */
@FeignClient(url="${csdn.server.ip}" ,path = "/datacenter/api/v1/auth" ,name ="data-center-token",configuration = DataCenterFeignTokenConfig.class)
public interface DataCenterTokenRemoteApi {

    /**
     * 获取数据中心token
     */
    @PostMapping("/getToken")
    CsdnBaseResponse<DataCenterTokenResponse> getToken(DataCenterTokenRequest requestToken);


}
