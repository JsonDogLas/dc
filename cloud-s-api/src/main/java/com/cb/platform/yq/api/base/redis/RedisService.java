package com.cb.platform.yq.api.base.redis;

import com.alibaba.fastjson.JSON;
import com.baidubce.util.JsonUtils;
import com.cb.platform.yq.api.base.redis.prefix.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取对象
     * @param prefix
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public <T>T get(KeyPrefix prefix, String key, Class<T> cls){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            key = prefix.getPrefix()+key;

            String value = jedis.get(key);
            T t = strToBean(value,cls);
            return t;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T>boolean set(KeyPrefix prefix,String key, T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String sValue = beanToStr(value);

            key = prefix.getPrefix()+key;
            int second = prefix.expireSeconds();
            if(second <= 0){
                if(!exit(prefix,key)) {
                    jedis.set(key, sValue);
                }
            }else{
                jedis.setex(key,second,sValue);
            }
            return true;
        }catch (Exception e){
             e.printStackTrace();
            return true;
        } finally{
            returnToPool(jedis);
        }
    }

    /**
     * 判断是否存在
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exit(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            key = prefix.getPrefix() + key;
            return jedis.exists(key);
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * 删除key
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long del(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            key = prefix.getPrefix() + key;
            return jedis.del(key);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 类转str
     * @param value
     * @param <T>
     * @return
     */
    private <T> String beanToStr(T value) {
        if(value == null ){
            return null;
        }
        Class<?>cls = value.getClass();
        if(cls == int.class || cls == Integer.class){
            return ""+value;
        }else if(cls == String.class){
            return (String)value;
        }else if(cls == long.class || cls == Long.class){
            return ""+value;
        }
        return JSON.toJSONString(value);
    }

    /**
     * 字符串转Bean
     * @param str
     * @param <T>
     * @return
     */
    private <T> T strToBean(String str,Class<T> cls) {
        if(str == null || str.length() <=0 || cls == null){
            return null;
        }
        if(cls == int.class || cls == Integer.class){
            return (T)Integer.valueOf(str) ;
        }else if(cls == String.class){
            return (T)str;
        }else if(cls == long.class || cls == Long.class){
            return (T)Long.valueOf(str);
        }
        //return JsonUtils.fromJsonString(str,cls);
        return JSON.toJavaObject(JSON.parseObject(str),cls);
    }

    /**
     * 返回连接池
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if(jedis !=null){
            jedis.close();
        }
    }
}
