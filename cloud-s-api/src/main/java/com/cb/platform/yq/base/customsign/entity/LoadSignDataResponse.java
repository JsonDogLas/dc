package com.cb.platform.yq.base.customsign.entity;







import com.cb.platform.yq.api.utils.ID;

import java.util.List;

/**
 * 加载签名数据返回
 */
public class LoadSignDataResponse {
    private List<Stamp> stampList;//签章集合
    private List<PdfImage> pdfImageList;//转换图片
    private SignFileInfo signFileInfo;//文件信息
    private String handSignKey;//手写签名标识
    private boolean applyStampFlag;//是否需要申请印章
    private List<String> permissionList;//拥有的权限
    private List<String> handWriteStamp;//手写体章id集合

    public List<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<String> permissionList) {
        this.permissionList = permissionList;
    }

    public boolean getApplyStampFlag() {
        return applyStampFlag;
    }

    public void setApplyStampFlag(boolean applyStampFlag) {
        this.applyStampFlag = applyStampFlag;
    }

    public LoadSignDataResponse() {
        this.handSignKey = ID.getGuid();
    }

    public List<Stamp> getStampList() {
        return stampList;
    }

    public void setStampList(List<Stamp> stampList) {
        this.stampList = stampList;
    }

    public List<PdfImage> getPdfImageList() {
        return pdfImageList;
    }

    public void setPdfImageList(List<PdfImage> pdfImageList) {
        this.pdfImageList = pdfImageList;
    }

    public SignFileInfo getSignFileInfo() {
        return signFileInfo;
    }

    public void setSignFileInfo(SignFileInfo signFileInfo) {
        this.signFileInfo = signFileInfo;
    }

    public String getHandSignKey() {
        return handSignKey;
    }

    public void setHandSignKey(String handSignKey) {
        this.handSignKey = handSignKey;
    }

    public boolean isApplyStampFlag() {
        return applyStampFlag;
    }

    public List<String> getHandWriteStamp() {
        return handWriteStamp;
    }

    public void setHandWriteStamp(List<String> handWriteStamp) {
        this.handWriteStamp = handWriteStamp;
    }
}
