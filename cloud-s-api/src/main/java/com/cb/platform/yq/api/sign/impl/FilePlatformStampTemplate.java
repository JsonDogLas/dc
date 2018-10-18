package com.cb.platform.yq.api.sign.impl;

import com.cb.platform.yq.api.entity.ApiClientInfoDo;
import com.cb.platform.yq.api.enums.ClientKeyTypeEnum;
import com.cb.platform.yq.api.sign.SignDataService;
import com.cb.platform.yq.api.sign.StampTemplate;
import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.customsign.enums.StampFromEnum;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import com.ceba.base.exception.BusinessErrorInfoException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电子文件平台的签章
 */
public class FilePlatformStampTemplate  extends AbstractStampTemplate{

    /**
     * keyId
     */
    private String keyId;


    /**
     * 签名数据服务
     */
    private SignDataService signDataService;

    /**
     * api用户
     */
    private String apiUserId;

    /**
     * 客户端信息
     */
    private ApiClientInfoDo apiClientInfoDo;

    /**
     * 系统已经加载好的签章
     */
    private List<Stamp> stampList;


    public FilePlatformStampTemplate(String keyId, SignDataService signDataService, String apiUserId, ApiClientInfoDo apiClientInfoDo) {
        this.keyId = keyId;
        this.signDataService = signDataService;
        this.apiUserId = apiUserId;
        this.apiClientInfoDo = apiClientInfoDo;
    }

    public FilePlatformStampTemplate(String keyId, SignDataService signDataService, String apiUserId, ApiClientInfoDo apiClientInfoDo,List<Stamp> stampList) {
        this(keyId,signDataService,apiUserId,apiClientInfoDo);
        this.stampList = stampList;
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

    @Override
    public StampInfo tackStamp() throws BusinessErrorInfoException {
        if(apiClientInfoDo.getClientKeyType().contains(ClientKeyTypeEnum.FILE_PLAT_STAMP.getFlag())){
            SystemProperties.log(logger,"公司名称:"+apiClientInfoDo.getCompanyName()+"keyId:"+keyId+"加载电子文件平台印章");
            Map<Object, Object> dataMap=new HashMap<>();
            dataMap.put("keyId",keyId);
            if(CollectionUtils.isNotEmpty(stampList)){
                return thirdPartyStamp(SystemProperties._filePlatStampUrl,dataMap,apiUserId, StampFromEnum.FILE_PLAT,stampList);
            }
            return thirdPartyStamp(SystemProperties._filePlatStampUrl,dataMap,apiUserId, StampFromEnum.FILE_PLAT);
        }else{
            SystemProperties.log(logger,"电子文件平台加载印章未开启");
        }
        return null;
    }

    /**
     * 印章来源信息
     *
     * @return
     */
    @Override
    public String stampSourceInfo() {
        return "公司名称:"+apiClientInfoDo.getCompanyName()+"keyId:"+keyId+"加载电子文件平台印章";
    }
}
