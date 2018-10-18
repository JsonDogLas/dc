package com.cb.platform.yq.api.sign.bean.impl;

import com.cb.platform.yq.base.customsign.entity.SignFileInfo;
import com.cb.platform.yq.base.customsign.enums.BlueDigitalEnum;
import org.apache.commons.lang.StringUtils;

public class SignFileInfoDTO extends SignFileInfo {
    private String signFileId;//文件ID-转图片需要的文件ID

    private String virtualSignFileId;//虚拟签名ID //加载文件图片 传 签名文件虚拟ID  防止 保存后重复打开 需要传

    private String title;



    public String getSignFileId() {
        return signFileId;
    }

    public void setSignFileId(String signFileId) {
        this.signFileId = signFileId;
    }

    public String getVirtualSignFileId() {
        return virtualSignFileId;
    }

    public void setVirtualSignFileId(String virtualSignFileId) {
        this.virtualSignFileId = virtualSignFileId;
    }

    @Override
    public String getTitle() {
        if(StringUtils.isNotEmpty(title)){
            return title;
        }
        return super.getTitle();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 设置父类
     * @param signFileInfo
     */
    public void setSignFileInfo(SignFileInfo signFileInfo){
        this.setVirtualPath(signFileInfo.getVirtualPath());
        this.setFilePath(signFileInfo.getFilePath());
        this.setOriginalPath(signFileInfo.getOriginalPath());
        this.setFileName(signFileInfo.getFileName());
        this.setTotalPageCount(signFileInfo.getTotalPageCount());

        this.setHasSm2Signed(signFileInfo.getHasSm2Signed());
        this.setHasRSASigned(signFileInfo.getHasRSASigned());
        this.setBlueDigital(signFileInfo.getBlueDigital());
        this.setFileId(signFileInfo.getFileId());
    }
}
