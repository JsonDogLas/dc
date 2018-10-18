package com.cb.platform.yq.api.service.impl;

import com.cb.platform.yq.api.dao.ApiSignFileLogDao;
import com.cb.platform.yq.api.entity.ApiGFileDo;
import com.cb.platform.yq.api.entity.ApiSignFileLogDo;
import com.cb.platform.yq.api.service.ApiSignFileLogService;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.ceba.base.exception.BusinessErrorInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 签名日志服务实现
 * @author whh
 */
@Service
public class ApiSignFileLogServiceImpl implements ApiSignFileLogService {

    @Autowired
    private ApiSignFileLogDao apiSignFileLogDao;
    /**
     * 插入签名日志
     *
     * @param apiSignFileLogDo
     * @return
     */
    @Override
    public boolean insertSignFileLog(ApiSignFileLogDo apiSignFileLogDo) throws BusinessErrorInfoException{
        try{
            ApiSignFileLogDo _apiSignFileLogDo= apiSignFileLogDao.save(apiSignFileLogDo);
            if(_apiSignFileLogDo != null){
                return true;
            }
        }catch (Exception exceptions){
            throw new BusinessErrorInfoException(ApiErrorEnum.CE_SIGN_LOG_ERROR);
        }
        return false;
    }


}
