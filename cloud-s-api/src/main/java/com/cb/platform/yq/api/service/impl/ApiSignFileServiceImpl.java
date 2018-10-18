package com.cb.platform.yq.api.service.impl;

import com.cb.platform.yq.api.dao.ApiSignFileDao;
import com.cb.platform.yq.api.entity.ApiSignFileDo;
import com.cb.platform.yq.api.service.ApiSignFileService;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.ceba.base.exception.BusinessErrorInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 签名文件服务
 * @author whh
 */
@Service
public class ApiSignFileServiceImpl implements ApiSignFileService {

    @Autowired
    private ApiSignFileDao apiSignFileDao;

    /**
     * 插入签名日志
     * @param apiSignFileDo
     * @return
     * @throws BusinessErrorInfoException
     */
    @Override
    public boolean insertApiSignFileDo(ApiSignFileDo apiSignFileDo) throws BusinessErrorInfoException {
        try{
            ApiSignFileDo apiSignFileDo_ = apiSignFileDao.save(apiSignFileDo);
            if(apiSignFileDo_ != null){
                return true;
            }
        }catch(Exception e){
            throw new BusinessErrorInfoException(ApiErrorEnum.NO_URL_EXCEPTION);
        }
        return false;
    }
}
