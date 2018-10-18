package com.cb.platform.yq.api.sign;

import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存中签名上下文管理
 */
public abstract class MemorySignContextManager  implements SignContextManager{
    public static Logger logger = LoggerFactory.getLogger(MemorySignContextManager.class);
    /**
     * 签名上下文存储
     */
    private Map<String,SignContext> signContextMap;

    public MemorySignContextManager() {
        this.signContextMap = new ConcurrentHashMap<>();
    }

    /**
     * 新增一个签名上下文对象
     *
     * @param signSessionId
     * @param signContext
     */
    @Override
    public  void put(String signSessionId, SignContext signContext) {
        signContextMap.put(signSessionId,signContext);
        SystemProperties.log(logger,"signContextMap的大小["+this.signContextMap.size()+"]");
    }

    /**
     * 取得一个签名上下文对象
     *
     * @param signSessionId
     */
    @Override
    public  SignContext get(String signSessionId) {
        return signContextMap.get(signSessionId);
    }

    /**
     * 删除签名上下问
     *
     * @param signSessionId
     */
    @Override
    public  SignContext remove(String signSessionId) {
        return signContextMap.remove(signSessionId);
    }

    /**
     * 放入内存中
     * @param signSessionId
     * @param signContext
     */
    public  void memoryPut(String signSessionId,SignContext signContext){
        signContextMap.put(signSessionId,signContext);
        SystemProperties.log(logger,"signContextMap的大小["+this.signContextMap.size()+"]");
    }

    /**
     * 从内存中删除
     * @param signSessionId
     */
    public  void memoryRemove(String signSessionId){
        signContextMap.remove(signSessionId);
    }

    /**
     * 获取内存中所有的数据
     * @return
     */
    public Collection<SignContext> memoryAllData(){
        if(signContextMap.size() == 0){
            return null;
        }else{
            List<SignContext> signContextList=new ArrayList<>();
            return signContextMap.values();
        }
    }
}
