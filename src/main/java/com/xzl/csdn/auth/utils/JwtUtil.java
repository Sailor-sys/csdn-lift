package com.xzl.csdn.auth.utils;

import com.xzl.csdn.auth.model.JwtUser;
import com.xzl.csdn.auth.service.JwtTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: liupu
 * @description:
 * @date: 2020/12/12
 */
@Component
public class JwtUtil {

    @Autowired
    private JwtTokenService jwtTokenService;

    private static final String TOKEN_HEAD = "Authorization";

    @Value("${token.prefix:Bearer }")
    private String tokenPrefix;

    public JwtUser getJwtUser(HttpServletRequest request) {
        String tokenHead = request.getHeader(TOKEN_HEAD);
        if (StringUtils.isEmpty(tokenHead)) {
            return null;
        }
        //将头部Bearer 替换为空字符串，获取到实际的token数据
        String tokenValue = tokenHead.replace(tokenPrefix, "");
        //校验token是否过期
        if (jwtTokenService.isTokenExpired(tokenValue)) {
            return null;
        }
        return getJwtUser(tokenValue);
    }

    public JwtUser getJwtUser(String tokenValue) {

        //转换token->jwtUser
        JwtUser result = jwtTokenService.parseJwtUser(tokenValue);
        if (result == null) {
            //throw new exception
            return null;
        }
        return result;
    }
}
