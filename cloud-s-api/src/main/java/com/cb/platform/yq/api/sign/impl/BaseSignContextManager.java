package com.cb.platform.yq.api.sign.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cb.platform.yq.api.base.redis.RedisService;
import com.cb.platform.yq.api.base.redis.prefix.impl.WebUserPrefix;
import com.cb.platform.yq.api.sign.MemorySignContextManager;
import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.api.sign.bean.impl.SignGetReadyDataDTO;
import com.cb.platform.yq.base.Result;
import com.cb.platform.yq.base.customsign.entity.LoadSignDataResponse;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Proxy;

/**
 * 签名上下文管理
 * @author whh
 */
@Service
public class BaseSignContextManager extends MemorySignContextManager{
    public static Logger logger = LoggerFactory.getLogger(BaseSignContextManager.class);
    /**
     * 缓存
     */
    @Autowired
    private RedisService redisService;
    /**
     * 缓存前缀
     */
    private WebUserPrefix webUserPrefix;
    public BaseSignContextManager(){
        webUserPrefix=WebUserPrefix.apiSignContextInfo;
    }
    /**
     * 新增一个签名上下文对象
     *
     * @param signSessionId
     * @param signContext
     */
    @Override
    public void put(String signSessionId, SignContext signContext) {
        if(super.get(signSessionId) != null){
            super.put(signSessionId,signContext);
        }
        //每次都会与 redis同步更新
        redisService.set(webUserPrefix,signSessionId,signContext);
    }

    /**
     * 取得一个签名上下文对象
     *
     * @param signSessionId
     */
    @Override
    public SignContext get(String signSessionId) {
        SignContext signContext=super.get(signSessionId);
        if(signContext == null){
            signContext =redisService.get(webUserPrefix,signSessionId,SignContext.class);
            if(signContext == null){
                return null;
            }
            Object object=signContext.getSignGetReadyData();
            if(object instanceof Proxy){
                JSONObject jsonObject=(JSONObject)Proxy.getInvocationHandler(object);
                SignGetReadyDataDTO signGetReadyData=JSON.toJavaObject(jsonObject,SignGetReadyDataDTO.class);
                signContext.setSignGetReadyData(signGetReadyData);
            }
            Result result=signContext.getLoadSignDataResult();
            if(result != null){
                object=result.getData();
                if(object instanceof JSONObject){
                    JSONObject jsonObject=(JSONObject)object;
                    LoadSignDataResponse loadSignDataResponse=JSON.toJavaObject(jsonObject,LoadSignDataResponse.class);
                    result.setData(loadSignDataResponse);
                }
            }
            return signContext;
        }
        return signContext;
    }


    /**
     * 删除签名上下问
     * @param signSessionId
     */
    @Override
    public SignContext remove(String signSessionId) {
        SignContext signContext=super.remove(signSessionId);
        if(signContext == null){
            SystemProperties.log(logger,"签名会话["+signSessionId+"]签名上下文内存中已清除");
        }
        return signContext;
    }

}
