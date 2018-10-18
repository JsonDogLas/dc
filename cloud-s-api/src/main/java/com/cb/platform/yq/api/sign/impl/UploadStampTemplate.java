package com.cb.platform.yq.api.sign.impl;

import com.cb.platform.yq.api.sign.SignDataService;
import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.cb.platform.yq.api.utils.StampUtils;
import com.cb.platform.yq.api.vo.UploadStampVO;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.customsign.entity.StampTypeEnum;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;
import com.ceba.base.exception.BusinessErrorInfoException;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 上传印章
 * @author whh
 */
public class UploadStampTemplate extends AbstractStampTemplate {
    private SignDataService signDataService;
    private List<UploadStampVO> uploadStampVOList;
    private String apiUserId;


    public UploadStampTemplate(SignDataService signDataService, List<UploadStampVO> uploadStampVOList, String apiUserId) {
        this.signDataService = signDataService;
        this.uploadStampVOList = uploadStampVOList;
        this.apiUserId = apiUserId;
    }

    /**
     * 获取签名资源服务
     *
     * @return
     */
    @Override
    public SignDataService signDataService() {
        return this.signDataService;
    }

    /**
     * 获取签章
     *
     * @return
     */
    @Override
    public StampInfo tackStamp() throws BusinessErrorInfoException {
        if(CollectionUtils.isEmpty(uploadStampVOList)){
            return null;
        }
        List<Stamp> list=new ArrayList<>();
        for(UploadStampVO uploadStampVO:uploadStampVOList){
            String path= YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.BROWSER,FileVisitEnum.VIRTUAL,uploadStampVO.getImagePath());
            Stamp stamp=createStamp(path,1,apiUserId, StampUtils.fileName(path),StampTypeEnum.getCode(uploadStampVO.getStampType()));
            list.add(stamp);
        }
        return createStampInfo(list) ;
    }

    /**
     * 印章来源信息
     *
     * @return
     */
    @Override
    public String stampSourceInfo() {
        return "用户ID【"+apiUserId+"】的上传印章";
    }

    public SignDataService getSignDataService() {
        return signDataService;
    }

    public void setSignDataService(SignDataService signDataService) {
        this.signDataService = signDataService;
    }

    public List<UploadStampVO> getUploadStampVOList() {
        return uploadStampVOList;
    }

    public void setUploadStampVOList(List<UploadStampVO> uploadStampVOList) {
        this.uploadStampVOList = uploadStampVOList;
    }

    public String getApiUserId() {
        return apiUserId;
    }

    public void setApiUserId(String apiUserId) {
        this.apiUserId = apiUserId;
    }
}
