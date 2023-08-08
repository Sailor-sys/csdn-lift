package com.xzl.csdn.common;

import com.xzl.zq.remote.common.token.RedisTokenCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisCluster;

/**
 * @author gll
 * 2020/2/17 13:25
 */
@Configuration
public class RedisTokenServiceConfiguration {

    @Autowired
    private JedisCluster jedisCluster;

    @Value("${spring.application.name}")
    private String applicationName;


    @Bean
    public RedisTokenCacheService tokenCacheService() {
        return new RedisTokenCacheService(jedisCluster, applicationName);
    }

}
