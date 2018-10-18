package com.cb.platform.yq.api.sign.impl;

import com.cb.platform.yq.api.sign.SignDataService;
import com.cb.platform.yq.api.sign.StampTemplate;
import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.customsign.entity.StampTypeEnum;
import com.ceba.base.exception.BusinessErrorInfoException;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面上传签章
 */
public class PageUploadStampTemplate  extends AbstractStampTemplate {

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * api用户Id
     */
    private String apiUserId;

    /**
     * 签名数据
     */
    private SignDataService signDataService;

    /**
     * 印章类型
     */
    private StampTypeEnum stampTypeEnum;

    /**
     * 印章名称
     */
    private String stampName;

    public PageUploadStampTemplate(String filePath, String apiUserId, SignDataService signDataService, StampTypeEnum stampTypeEnum, String stampName) {
        this.filePath = filePath;
        this.apiUserId = apiUserId;
        this.signDataService = signDataService;
        this.stampTypeEnum = stampTypeEnum;
        this.stampName = stampName;
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
     * 获取签章
     *
     * @return
     */
    @Override
    public StampInfo tackStamp() throws BusinessErrorInfoException {
        Stamp stamp = createStamp(filePath,1,apiUserId,stampName,stampTypeEnum);
        List<Stamp> stampList=new ArrayList<>();
        stampList.add(stamp);
        return createStampInfo(stampList);
    }

    /**
     * 印章来源信息
     *
     * @return
     */
    @Override
    public String stampSourceInfo() {
        return "用户ID【"+apiUserId+"】的页面上传印章";
    }
}
