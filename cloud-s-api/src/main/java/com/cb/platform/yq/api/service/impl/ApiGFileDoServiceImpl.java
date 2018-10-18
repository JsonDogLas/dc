package com.cb.platform.yq.api.service.impl;

import com.cb.platform.yq.api.dao.ApiGFileDao;
import com.cb.platform.yq.api.entity.ApiGFileDo;
import com.cb.platform.yq.api.service.ApiGFileDoService;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.ceba.base.exception.BusinessErrorInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * api系统文件服务
 * @author whh
 */
@Service
public class ApiGFileDoServiceImpl implements ApiGFileDoService {

    @Autowired
    private ApiGFileDao apiGFileDao;

    /**
     * 创建文件
     *
     * @param apiGFileDo
     * @return
     */
    @Override
    public ApiGFileDo createApiGFileDo(ApiGFileDo apiGFileDo)throws BusinessErrorInfoException {
        try{
            return apiGFileDao.save(apiGFileDo);
        }catch (Exception e){
            throw new BusinessErrorInfoException(ApiErrorEnum.NO_URL_EXCEPTION);
        }
    }

    /**
     * 查询文件
     * @param filePath
     * @param flag
     * @return
     */
    @Override
    public ApiGFileDo sreachApiGFileDo(String filePath, String flag)throws BusinessErrorInfoException {
        try{
            return apiGFileDao.sreachApiGFileDo(filePath,flag);
        }catch (Exception e){
            throw new BusinessErrorInfoException(ApiErrorEnum.NO_URL_EXCEPTION);
        }
    }
}
