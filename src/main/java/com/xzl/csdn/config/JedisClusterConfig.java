package com.xzl.csdn.config;

import com.google.common.collect.Lists;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
@ConditionalOnClass({ JedisCluster.class })
public class JedisClusterConfig {
    @Value("${spring.redis.cache.clusterNodes}")
    private String clusterNodes;
    @Value("${spring.redis.cache.commandTimeout}")
    private Integer commandTimeout;

    @Value("${spring.redis.cache.password:}")
    private String password;

    @Bean
    public JedisCluster getJedisCluster() {
        String[] serverArray = clusterNodes.split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        if(Strings.isEmpty(password)){
            return new JedisCluster(nodes, commandTimeout);
        }else{
            return new JedisCluster(nodes,commandTimeout,commandTimeout,5,password,new GenericObjectPoolConfig());
        }
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(Lists.newArrayList(clusterNodes.split(",")));
        if(!Strings.isEmpty(password)){
            clusterConfiguration.setPassword(RedisPassword.of(password));
        }

        JedisConnectionFactory factory = new JedisConnectionFactory(clusterConfiguration,config());

        return factory;
    }
    private JedisPoolConfig config(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(-1);
        config.setMaxTotal(1000);
        config.setMinIdle(8);
        config.setMaxIdle(100);
        return config;
    }
}
