package com.cb.platform.yq.api.service;

import com.cb.platform.yq.api.entity.ApiClientInfoDo;
import com.ceba.base.exception.BusinessErrorInfoException;

/**
 * 客户端信息接口
 * @author whh
 */
public interface ApiClientInfoService {

    /**
     * 根据客户单id取的客户端信息
     * @param appId
     * @return
     */
    ApiClientInfoDo getByAppId(String appId) throws BusinessErrorInfoException;
}
