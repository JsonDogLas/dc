package com.cb.platform.yq.api.service;

import com.cb.platform.yq.api.entity.ApiSignFileDo;
import com.ceba.base.exception.BusinessErrorInfoException;

/**
 * 签名文件服务
 * @author whh
 */
public interface ApiSignFileService {

    /**
     * 插入签名日志
     * @param apiSignFileDo
     * @return
     * @throws BusinessErrorInfoException
     */
    boolean insertApiSignFileDo(ApiSignFileDo apiSignFileDo) throws BusinessErrorInfoException;
}
