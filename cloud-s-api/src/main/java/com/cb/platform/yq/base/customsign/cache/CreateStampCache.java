package com.cb.platform.yq.base.customsign.cache;

import com.cb.platform.yq.api.base.redis.RedisService;
import com.cb.platform.yq.api.base.redis.prefix.impl.WebUserPrefix;
import com.cb.platform.yq.base.customsign.entity.CreateStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateStampCache {
    @Autowired
    private RedisService redisService;

    private static CreateStampCache createStampCache;

    /**
     * 取得缓存对象
     * @return
     */
    public static CreateStampCache getInstance(){
        if(createStampCache == null){
            createStampCache=new CreateStampCache();
        }else{
            return createStampCache;
        }
        return null;
    }


    public CreateStamp get(String key){
        return redisService.get(WebUserPrefix.stampPrefix,key,CreateStamp.class);
    }


    public boolean put(String key,CreateStamp createStamp){
        return redisService.set(WebUserPrefix.stampPrefix,key,createStamp);
    }

}
