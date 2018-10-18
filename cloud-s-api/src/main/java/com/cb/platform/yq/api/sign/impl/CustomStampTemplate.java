package com.cb.platform.yq.api.sign.impl;

import com.cb.platform.yq.api.sign.SignDataService;
import com.cb.platform.yq.api.sign.bean.StampInfo;
import com.cb.platform.yq.api.sign.bean.impl.SignContext;
import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.api.utils.StampUtils;
import com.cb.platform.yq.base.customsign.entity.Stamp;
import com.cb.platform.yq.base.customsign.entity.StampTypeEnum;
import com.cb.platform.yq.base.exception.ApiErrorEnum;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.cb.platform.yq.base.interfaces.constant.InterfaceConstant;
import com.ceba.base.exception.BusinessErrorInfoException;
import com.ceba.base.stamp.StampFontFactory;
import com.ceba.base.utils.CreateQRCode;
import com.ceba.base.utils.IDSDateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义印章
 * 包含 透明章，二维码，日期章
 */
public class CustomStampTemplate extends AbstractStampTemplate {

    /**
     * 签名数据服务
     */
    private SignDataService signDataService;

    /**
     * api用户
     */
    private String apiUserId;

    /**
     * 二维码章的缩放比率
     */
    private float qrcodeStampZoom;

    /**
     * 日期章的缩放比率
     */
    private float dateStampZoom;

    /**
     * 签名上下文
     */
    private SignContext signContext;


    public CustomStampTemplate(SignDataService signDataService, String apiUserId,float qrcodeStampZoom,float dateStampZoom,SignContext signContext) {
        this.signDataService = signDataService;
        this.apiUserId = apiUserId;
        this.qrcodeStampZoom=qrcodeStampZoom;
        this.dateStampZoom=dateStampZoom;
        this.signContext=signContext;
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
     * @return
     */
    @Override
    public StampInfo tackStamp() throws BusinessErrorInfoException {
        List<Stamp> stampList=new ArrayList<>();
        //TODO 权限控制 二维码
        if(signContext.isPermission("ROLE_API_OPERATE_CODE")){
            String context=qrcodeContext();
            String filepath= YqFilePathEnum.YZ.getFilePath(FileVisitEnum.ABSOLUTE,ID.getGuid())+ ID.getGuid()+".png";
            CreateQRCode.encoderQRCode(context, filepath, "png",7);
            //String filePath,float stampZoom,String apiUserId,String stampName,String stampType
            Stamp qrcodeStamp=createStamp(filepath,qrcodeStampZoom,apiUserId,"二维码", StampTypeEnum.QRCODE);
            stampList.add(qrcodeStamp);
        }
        //透明章
        String filepath= YqFilePathEnum.YZ.getFilePath(FileVisitEnum.ABSOLUTE,InterfaceConstant.SYSTEM_SUPER_ADMIN_USER_ID)+ InterfaceConstant.SYSTEM_SUPER_ADMIN_USER_ID+".png";
        boolean bool=true;
        if(!new File(filepath).exists()){
            try {
                StampUtils.transparentStamp(filepath);
            } catch (Exception e) {
                throw new BusinessErrorInfoException(ApiErrorEnum.CE_TRANSPARENT_STAMP_FAIL);
            }
            bool=false;
        }
        Stamp transparentStamp=createStamp(bool,filepath,1,apiUserId,"透明章", StampTypeEnum.TRANSPARENT);
        stampList.add(transparentStamp);
        //日期章
        if(signContext.isPermission("ROLE_API_OPERATE_DATE")){
            String nowDateStr= IDSDateUtils.getNowTime(DATE_TYPE);
            String fileName=nowDateStr.replace("/","-")+"."+DATE_FILE_TYPE;
            //避免日期章重复 文件名取 hash
            filepath= YqFilePathEnum.YZ.getFilePath(FileVisitEnum.ABSOLUTE, InterfaceConstant.SYSTEM_SUPER_ADMIN_USER_ID)+ fileName;
            bool=true;
            if(!new File(filepath).exists()) {
                StampFontFactory.createDateStamp(filepath, nowDateStr);
                bool=false;
            }
            Stamp dateStamp=createStamp(bool,filepath,dateStampZoom,apiUserId,nowDateStr,StampTypeEnum.DATE);
            stampList.add(dateStamp);
        }
        return createStampInfo(stampList);
    }

    /**
     * 二维码内容
     * @return
     */
    public String qrcodeContext(){
        return "test";
    }


    /**
     * 印章来源信息
     *
     * @return
     */
    @Override
    public String stampSourceInfo() {
        return "用户ID【"+apiUserId+"】的自定义印章";
    }

    public SignDataService getSignDataService() {
        return signDataService;
    }

    public void setSignDataService(SignDataService signDataService) {
        this.signDataService = signDataService;
    }

    public String getApiUserId() {
        return apiUserId;
    }

    public void setApiUserId(String apiUserId) {
        this.apiUserId = apiUserId;
    }

    public float getQrcodeStampZoom() {
        return qrcodeStampZoom;
    }

    public void setQrcodeStampZoom(float qrcodeStampZoom) {
        this.qrcodeStampZoom = qrcodeStampZoom;
    }

    public float getDateStampZoom() {
        return dateStampZoom;
    }

    public void setDateStampZoom(float dateStampZoom) {
        this.dateStampZoom = dateStampZoom;
    }
}
