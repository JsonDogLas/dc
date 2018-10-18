package com.cb.platform.yq.api.sign;

import com.cb.platform.yq.api.sign.bean.SignGetReadyData;
import com.cb.platform.yq.api.sign.bean.impl.SignCompleteData;
import com.cb.platform.yq.base.Result;


/**
 * 签名接口
 */
public interface SignApi{

    /**
     * 上传签名文件信息
     * @param signGetReadyData
     * @return
     */
    Result uploadFile(SignGetReadyData signGetReadyData);


    /**
     * 根据签名会话返回签名文件
     * @param signSessionId
     * @return
     */
    Result signData(String signSessionId);
}
