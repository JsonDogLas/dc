package com.cb.platform.yq.api.sign.impl;

import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.web.response.IResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Future;

/**
 * 内存中签名上下文管理
 */
@Service
public  class MemoryFutureManager{
    public static Logger logger = LoggerFactory.getLogger(MemoryFutureManager.class);
    /**
     * 签名上下文存储
     */
    private Map<String,Future<IResult>> futureMap;

    public MemoryFutureManager() {
        this.futureMap = new HashMap<>();
    }

    /**
     * 新增一个签名上下文对象
     *
     * @param key
     * @param future
     */
    public void put(String key, Future<IResult> future) {
        futureMap.put(key,future);
        SystemProperties.log(logger,"Future<IResult>Map的大小["+this.futureMap.size()+"]");
    }

    /**
     * 取得一个签名上下文对象
     *
     * @param key
     */
    public  Future<IResult> get(String key) {
        return futureMap.get(key);
    }

    /**
     * 删除签名上下问
     *
     * @param key
     */
    public  Future<IResult> remove(String key) {
        return futureMap.remove(key);
    }

    /**
     * 取出所有数据
     * @return
     */
    public Collection<Future<IResult>> all(){
        return futureMap.values();
    }

}
