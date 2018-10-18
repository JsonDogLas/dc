package com.cb.platform.yq.api.sign;

import com.cb.platform.yq.api.sign.bean.impl.SignContext;

/**
 * 签名上下问管理
 */
public interface SignContextManager {

    /**
     * 新增一个签名上下文对象
     * @param signSessionId
     */
    void put(String signSessionId,SignContext signContext);

    /**
     * 取得一个签名上下文对象
     * @param signSessionId
     */
    SignContext get(String signSessionId);

    /**
     * 删除签名上下问
     * @param signSessionId
     */
    SignContext remove(String signSessionId);

}
