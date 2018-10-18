package com.cb.platform.yq.api.sign.impl;

import com.cb.platform.yq.api.entity.ApiClientInfoDo;
import com.cb.platform.yq.api.enums.ClientKeyTypeEnum;
import com.cb.platform.yq.api.sign.SignDataService;
import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.customsign.enums.StampFromEnum;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.exception.BusinessErrorInfoException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三方加载签章
 * @author whh
 */
public class  ThirdPartyStampTemplate extends AbstractStampTemplate {
    public static Logger logger = LoggerFactory.getLogger(ThirdPartyStampTemplate.class);


    /**
     * 签名数据服务
     */
    private SignDataService signDataService;

    /**
     * 客户端信息
     */
    private ApiClientInfoDo apiClientInfoDo;

    /**
     * token
     */
    private String token;

    /**
     * keyId
     */
    private String keyId;

    /**
     * api用户ID
     */
    private String apiUserId;

    /**
     * 系统已经加载好的签章
     */
    private List<Stamp> stampList;


    public ThirdPartyStampTemplate(SignDataService signDataService, ApiClientInfoDo apiClientInfoDo, String token, String keyId, String apiUserId,List<Stamp> stampList) {
        this(signDataService,apiClientInfoDo,token,keyId,apiUserId);
        this.stampList = stampList;
    }

    public ThirdPartyStampTemplate(SignDataService signDataService, ApiClientInfoDo apiClientInfoDo, String token, String keyId, String apiUserId) {
        this.signDataService = signDataService;
        this.apiClientInfoDo = apiClientInfoDo;
        this.token = token;
        this.keyId = keyId;
        this.apiUserId = apiUserId;
    }

    /**
     * 获取签名资源服务
     *
     * @return
     */
    @Override
    public SignDataService signDataService() {
        return signDataService;
    }

    /**
     * 获取印章
     * @return
     */
    @Override
    public StampInfo tackStamp() throws BusinessErrorInfoException {
        if(apiClientInfoDo.getClientKeyType().contains(ClientKeyTypeEnum.CLIENT_PLAT_STAMP.getFlag())){
            if(StringUtils.isEmpty(apiClientInfoDo.getUrl())){
                throw new BusinessErrorInfoException(ApiErrorEnum.NO_URL_EXCEPTION);
            }
            SystemProperties.log(logger,"公司名称:"+apiClientInfoDo.getCompanyName()+"keyId:"+keyId+"加载印章");
            Map<Object, Object> dataMap=new HashMap<>();
            dataMap.put("keyId",keyId);
            dataMap.put("token",token);
            if(CollectionUtils.isNotEmpty(stampList)){
                return thirdPartyStamp(SystemProperties._filePlatStampUrl,dataMap,apiUserId, StampFromEnum.THREE_PLAT,stampList);
            }
            return thirdPartyStamp(apiClientInfoDo.getUrl(),dataMap,apiUserId, StampFromEnum.THREE_PLAT);
        }else{
            SystemProperties.log(logger,"第三方加载印章未开启");
        }
        return null;
    }

    /**
     * 印章来源信息
     * @return
     */
    @Override
    public String stampSourceInfo() {
        return "公司名称:"+apiClientInfoDo.getCompanyName()+"keyId:"+keyId+"加载第三方印章";
    }

}
