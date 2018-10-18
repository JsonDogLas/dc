package com.cb.platform.yq.api.service;

import com.cb.platform.yq.api.entity.ApiGFileDo;
import com.ceba.base.exception.BusinessErrorInfoException;

/**
 * api系统文件服务
 * @author whh
 */
public interface ApiGFileDoService {

    /**
     * 创建文件
     * @param apiGFileDo
     * @return
     */
    ApiGFileDo createApiGFileDo(ApiGFileDo apiGFileDo)throws BusinessErrorInfoException;


    /**
     * 查询文件
     * @param filePath
     * @param flag
     * @return
     */
    ApiGFileDo sreachApiGFileDo(String filePath, String flag)throws BusinessErrorInfoException;
}
