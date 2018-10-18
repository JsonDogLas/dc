package com.cb.platform.yq.api.sign.impl;

import abc.util.StringUtils;
import com.cb.platform.yq.api.sign.bean.impl.LoadFileImageDTO;
import com.cb.platform.yq.base.customsign.entity.LoadFileImageReqeust;
import com.cb.platform.yq.base.customsign.service.fileImage.AbstractLoadFileImage;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;

/**
 * api 加载文件图片
 * @author whh
 */
public class ApiLoadFileImage extends AbstractLoadFileImage {
    /**
     * PDF 图片 操作目录的 绝对路径
     * @param loadFileImageReqeust
     * @return
     */
    @Override
    public String pdfImageAbsoluteOperatePath(LoadFileImageReqeust loadFileImageReqeust) {
        LoadFileImageDTO loadFileImageDTO=null;
        if(loadFileImageReqeust instanceof LoadFileImageDTO){
            loadFileImageDTO=(LoadFileImageDTO)loadFileImageReqeust;
        }

        String id=loadFileImageDTO.getSignFileId();
        if(StringUtils.isEmpty(id)){
            id=loadFileImageDTO.getVirtualSignFileId();
        }
        return YqFilePathEnum.LOCAL_FILE_IMAGE.getFilePath(FileVisitEnum.ABSOLUTE,id);
    }

    /**
     * 虚拟路径转绝对路径
     *
     * @param loadFileImageReqeust
     * @param filePath
     * @return
     */
    @Override
    public String absoluteTobrowserPath(LoadFileImageReqeust loadFileImageReqeust, String filePath) {
        return YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.ABSOLUTE,FileVisitEnum.BROWSER,filePath);
    }
}
