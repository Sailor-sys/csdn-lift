package com.xzl.csdn.auth.security;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.xzl.csdn.auth.model.JwtUser;
import com.xzl.csdn.auth.model.UserTokenContext;
import com.xzl.csdn.auth.service.IgnoreUrlService;
import com.xzl.csdn.auth.utils.JwtUtil;
import com.xzl.csdn.support.ApiResult;
import com.xzl.csdn.support.ApiStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@DependsOn(value = "webSecurityConfig")
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private UsernamePasswordAuthenticationToken authentication = null;

    @Value("${csdn.system.name}")
    protected String systemEnv;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IgnoreUrlService ignoreUrlService;

    List<AntPathRequestMatcher> matchers = new ArrayList<>();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        if (!isIgnore(request)) {

            JwtUser result = jwtUtil.getJwtUser(request);
            if (result == null) {
                writeResponse(response, request);
                return;
            }
            //将result信息放到ThreadLocal中，后续请求可以通过UserTokenContext.get()获取到用户信息
            UserTokenContext.setUserInfo(result);

        }
        try {
            SecurityContextHolder.getContext().setAuthentication(getUnauthorizedAuthentication());
            chain.doFilter(request, response);
        } finally {
            UserTokenContext.clear();
        }
    }

    private void writeResponse(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //如果是ajax请求，返回一个json对象，告诉前端需要登录
        String redirectUrl = null;
        log.warn("write error response errorCode={},message={},url={}", ApiStatus.UNAUTHORIZED.getStatus(), ApiStatus.UNAUTHORIZED.getMessage(), request.getRequestURI());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        ApiResult apiResult = new ApiResult();
        apiResult.setStatus(ApiStatus.UNAUTHORIZED);
        Map<String, Object> data = new HashMap<>();
        apiResult.setData(data);
        response.getWriter().write(JSON.toJSONString(apiResult));
    }

    /**
     * 构建无权限的authentication,方便全局异常处理
     */
    public UsernamePasswordAuthenticationToken getUnauthorizedAuthentication() {
        if (this.authentication != null) {
            return this.authentication;
        }
        List list = new ArrayList();
        SimpleGrantedAuthority sga = new SimpleGrantedAuthority("Unauthorized");
        list.add(sga);
        UsernamePasswordAuthenticationToken tempAuthentication = new UsernamePasswordAuthenticationToken(
                null, null, list);
        this.authentication = tempAuthentication;
        return authentication;
    }


    @PostConstruct
    public void init() {
        for (String url : ignoreUrlService.methodGetIgnoreUrl()) {
            AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(url, HttpMethod.GET.name());
            matchers.add(antPathRequestMatcher);
        }
        for (String url : ignoreUrlService.allMethodIgnoreUrl()) {
            AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(url);
            matchers.add(antPathRequestMatcher);
        }
    }

    private boolean isIgnore(HttpServletRequest request) {
        for (AntPathRequestMatcher matcher : matchers) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        //白名单token
        List<String> ignoreTokens = Lists.newArrayList("eb5ba3e3724881f7a7e2524058eaf8aff23b72ba26470e8ed9e815568f523100", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMzgxMjEyMTIxMiIsIm1vYmlsZVBob25lIjoiMTM4MTIxMjEyMTIiLCJsb2dpblR5cGUiOjIsIm5pY2tOYW1lIjoi5YWN5a-G6LSm5Y-3IiwiYXBwVHlwZSI6bnVsbCwiY3JlYXRlZCI6MTY3MTYxNTkyMzQxNSwibG9naW5WZXJzaW9uIjoxLCJndWlkIjo2NSwidW5pdElkIjowLCJleHAiOjE2NzY3OTk5MjMsInV1aWQiOiIzMzA1MGYyZS1lYmZmLTQ0MWYtODNkNi0wNGRlNzQ5MTQxMDgifQ.WdVnb1ZQAJMjJrSZTPnRbc1ZKKKMgg9vKganSUt-TK0");
        List<String> ignoreToken1s = Lists.newArrayList("Bearer eb5ba3e3724881f7a7e2524058eaf8aff23b72ba26470e8ed9e815568f523100", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMzgxMjEyMTIxMiIsIm1vYmlsZVBob25lIjoiMTM4MTIxMjEyMTIiLCJsb2dpblR5cGUiOjIsIm5pY2tOYW1lIjoi5YWN5a-G6LSm5Y-3IiwiYXBwVHlwZSI6bnVsbCwiY3JlYXRlZCI6MTY3MTYxNTkyMzQxNSwibG9naW5WZXJzaW9uIjoxLCJndWlkIjo2NSwidW5pdElkIjowLCJleHAiOjE2NzY3OTk5MjMsInV1aWQiOiIzMzA1MGYyZS1lYmZmLTQ0MWYtODNkNi0wNGRlNzQ5MTQxMDgifQ.WdVnb1ZQAJMjJrSZTPnRbc1ZKKKMgg9vKganSUt-TK0");
        String tokenHead = request.getHeader("Authorization");
        if (ignoreTokens.contains(tokenHead) || (ignoreToken1s.contains(tokenHead))) {
            JwtUtil jwtUtil = SpringUtil.getBean(JwtUtil.class);
            JwtUser jwtUser = jwtUtil.getJwtUser("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMzgxMjEyMTIxMiIsIm1vYmlsZVBob25lIjoiMTM4MTIxMjEyMTIiLCJsb2dpblR5cGUiOjIsIm5pY2tOYW1lIjoi5YWN5a-G6LSm5Y-3IiwiYXBwVHlwZSI6bnVsbCwiY3JlYXRlZCI6MTY3MTYxNTkyMzQxNSwibG9naW5WZXJzaW9uIjoxLCJndWlkIjo2NSwidW5pdElkIjowLCJleHAiOjE2NzY3OTk5MjMsInV1aWQiOiIzMzA1MGYyZS1lYmZmLTQ0MWYtODNkNi0wNGRlNzQ5MTQxMDgifQ.WdVnb1ZQAJMjJrSZTPnRbc1ZKKKMgg9vKganSUt-TK0");
            UserTokenContext.setUserInfo(jwtUser);

            return true;
        }
        return false;
    }
}