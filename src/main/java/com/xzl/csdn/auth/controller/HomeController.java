package com.xzl.csdn.auth.controller;

import com.xzl.csdn.auth.codeVerification.ImageCodeResponse;
import com.xzl.csdn.auth.model.*;
import com.xzl.csdn.auth.service.JwtTokenService;
import com.xzl.csdn.auth.service.LoginService;
import com.xzl.csdn.support.RestApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author XZL
 */
@RestApi("/csdn/home")
@Slf4j
@Api(tags = "登录模块")
public class HomeController {

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @GetMapping("/getImageCode")
    @ApiOperation("获取图形验证码")
    public void getImageCode(HttpServletResponse response) throws IOException {
        ImageCodeResponse imageCode = loginService.getImageCode();
        //将图形验证码返回前端
        // header-token
        response.addHeader("imageCodeKey", imageCode.getImageCodeKey());

        // 将生成的图片写到接口的响应中
        ImageIO.write(imageCode.getImageCode().getImage(), "JPEG", response.getOutputStream());
    }

    /**
     * 登录
     */
    @PostMapping("/doLogin")
    @ApiOperation("登录")
    public CsdnUserVO doLogin(@RequestBody LoginParamQuery loginParamQuery) {
        return loginService.login(loginParamQuery);
    }

    /**
     * 登出
     */
    @PostMapping("/doLogout")
    @ApiOperation("登出")
    public void doLogout() {
        JwtUser userInfo = UserTokenContext.getUserInfo();
        jwtTokenService.forceLogout(userInfo.getUsername());
    }

    /**
     * 获取token
     */
    @PostMapping("/getToken")
    @ApiOperation("获取token")
    public CsdnUserVO getToken(@RequestBody LoginParamQuery loginParamQuery, HttpServletRequest request) {
        if (loginParamQuery.getLoginType() == null) {
            loginParamQuery.setLoginType(LoginTypeEnum.CLIENT_MODE.getCode());
        }
        return loginService.login(loginParamQuery);
    }

    /**
     * 根据token获取用户
     */
    @GetMapping("/getUserByToken")
    public CsdnUserVO getUserByToken(String token) {
        return loginService.getUserByToken(token);
    }

    @RequestMapping("/delRedis")
    public void delRedis(String cache, String key) {
        jedisCluster.hdel(cache, key);
    }

    @RequestMapping("/delRedis1")
    @ResponseBody
    public long delRedis1(String key) {
        return jedisCluster.del(key);
    }

}
