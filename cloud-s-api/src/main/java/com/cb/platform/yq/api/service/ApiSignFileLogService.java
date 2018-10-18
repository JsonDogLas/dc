package com.cb.platform.yq.api.service;

import com.cb.platform.yq.api.entity.ApiGFileDo;
import com.cb.platform.yq.api.entity.ApiSignFileLogDo;
import com.ceba.base.exception.BusinessErrorInfoException;

/**
 * 签名日志服务
 * @author whh
 */
public interface ApiSignFileLogService {


    /**
     * 插入签名日志
     * @param apiSignFileLogDo
     * @return
     */
    boolean insertSignFileLog(ApiSignFileLogDo apiSignFileLogDo)throws BusinessErrorInfoException;
}
