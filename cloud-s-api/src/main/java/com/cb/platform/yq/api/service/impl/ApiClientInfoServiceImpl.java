package com.cb.platform.yq.api.service.impl;

import com.cb.platform.yq.api.base.oauth.server.LoadClientPermissionService;
import com.cb.platform.yq.api.dao.ApiClientInfoDao;
import com.cb.platform.yq.api.entity.ApiClientInfoDo;
import com.cb.platform.yq.api.service.ApiClientInfoService;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.exception.BusinessErrorInfoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 客户端信息接口实现
 */
@Service
public class ApiClientInfoServiceImpl implements ApiClientInfoService {
    public static Logger logger = LoggerFactory.getLogger(ApiClientInfoServiceImpl.class);


    @Autowired
    private ApiClientInfoDao apiClientInfoDao;

    /**
     * 根据客户单id取的客户端信息
     *
     * @param appId
     * @return
     */
    @Override
    public ApiClientInfoDo getByAppId(String appId)  throws BusinessErrorInfoException {
        try{
            return apiClientInfoDao.getByAppId(appId);
        }catch(Exception e){
            if(SystemProperties.loggerFalg){
                logger.debug("客户端信息查询报错："+e.getMessage());
            }
            throw new BusinessErrorInfoException(ApiErrorEnum.CLIENT_INFO_BY_APPID_ERROR);
        }
    }
}
