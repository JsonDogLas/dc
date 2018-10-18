package com.cb.platform.yq.api.base.redis;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
public class JedisPoolFactory{
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        config.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
        config.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().toMillis());
        return config;
    }

    @Bean
    public JedisPool getJedisPool(){
        JedisPoolConfig config=getJedisPoolConfig();
        int time=Integer.parseInt(ObjectUtils.toString(redisProperties.getTimeout().getSeconds()))*1000;
        JedisPool pool = new JedisPool(config,redisProperties.getHost(),redisProperties.getPort(), time,
                redisProperties.getPassword(),redisProperties.getDatabase(),"demo");
        return pool;
    }

    /*public RedisStandaloneConfiguration redisStandaloneConfiguration(){
        RedisStandaloneConfiguration redisStandaloneConfiguration=new RedisStandaloneConfiguration(redisProperties.getHost(),redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        redisStandaloneConfiguration.setDatabase(redisStandaloneConfiguration.getDatabase());
        return redisStandaloneConfiguration;
    }*/



}
