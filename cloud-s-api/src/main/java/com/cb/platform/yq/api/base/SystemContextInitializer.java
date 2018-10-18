package com.cb.platform.yq.api.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;


@Component
public class SystemContextInitializer implements CommandLineRunner{
    @Override
    public void run(String... args) throws Exception {
        //全局关闭循环引用
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }
    public static void main(String[] star){
        String string=new BCryptPasswordEncoder().encode("111111");
        System.out.println(string);


        JedisShardInfo jedisShardInfo=new JedisShardInfo("192.168.0.121","6379");
        jedisShardInfo.setPassword("123456");
        //jedisShardInfo.setConnectionTimeout(20*1000);


        Jedis jedis = new Jedis(jedisShardInfo);
        String str=jedis.get("com.cb.platform.yq.api.base.redis.prefix.impl.WebUserPrefix:context:748a0ef788f94d8caa51dec4f6a61217");
        System.out.println(str);
    }


}
