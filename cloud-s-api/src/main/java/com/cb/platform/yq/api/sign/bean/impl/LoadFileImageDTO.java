package com.cb.platform.yq.api.sign.bean.impl;

import com.cb.platform.yq.base.customsign.entity.LoadFileImageReqeust;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;

/**
 * api加载图片请求
 */
public class LoadFileImageDTO extends LoadFileImageReqeust{
    //文件ID-转图片需要的文件ID
    private String signFileId;
    //虚拟签名ID //加载文件图片 传 签名文件虚拟ID  防止 保存后重复打开 需要传
    private String virtualSignFileId;
    @Override
    public String getTempPath() {
        return YqFilePathEnum.LOCAL_FILE_IMAGE.getFilePath(FileVisitEnum.ABSOLUTE,signFileId);
    }

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
}
