package com.cb.platform.yq.api.sign.impl;

import com.cb.platform.yq.api.sign.bean.impl.LoadFileImageDTO;
import com.cb.platform.yq.api.sign.bean.impl.SignFileInfoDTO;
import com.cb.platform.yq.base.customsign.entity.LoadFileImageReqeust;
import com.cb.platform.yq.base.customsign.entity.PdfImageParam;
import com.cb.platform.yq.base.customsign.entity.SignFileInfo;
import com.cb.platform.yq.base.customsign.service.DefaultLoadSignData;
import com.cb.platform.yq.base.customsign.service.fileImage.LoadFileImageBase;
import com.cb.platform.yq.base.customsign.service.fileImage.impl.DefaultLoadFileImage;
import com.ceba.base.exception.IDSException;

/**
 * api 加载签名数据
 * @author whh
 */
public class ApiLoadSignData extends DefaultLoadSignData {

    /**
     * 获取PDF文件信息
     *
     * @param path
     * @return
     * @throws IDSException
     */
    @Override
    public SignFileInfo getPdfFileInfo(String path) throws IDSException {
        return super.getPdfFileInfo(path);
    }

    /**
     * 加载文件图片
     * @param signFileInfo
     * @return
     * @throws Exception
     */
    @Override
    public PdfImageParam loadFileImage(SignFileInfo signFileInfo) throws IDSException {
        SignFileInfoDTO signFileInfoDTO=(SignFileInfoDTO)signFileInfo;
        LoadFileImageDTO loadFileImageReqeust=new LoadFileImageDTO();
        loadFileImageReqeust.setFileId(signFileInfo.getFileId());
        loadFileImageReqeust.setFilePath(signFileInfo.getFilePath());
        firstLoadPage(signFileInfo,loadFileImageReqeust);
        loadFileImageReqeust.setIsNeedNewPage(false);
        loadFileImageReqeust.setIsDeletePage(false);
        loadFileImageReqeust.setSignFileId(signFileInfoDTO.getSignFileId());
        loadFileImageReqeust.setVirtualSignFileId(signFileInfoDTO.getVirtualSignFileId());
        LoadFileImageBase loadFileImageBase= new ApiLoadFileImage();
        return loadFileImageBase.loadFileImage(loadFileImageReqeust);
    }
}
