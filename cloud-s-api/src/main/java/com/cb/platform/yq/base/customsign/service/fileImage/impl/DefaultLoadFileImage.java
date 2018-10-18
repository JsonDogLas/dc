package com.cb.platform.yq.base.customsign.service.fileImage.impl;

import com.cb.platform.yq.base.customsign.entity.LoadFileImageReqeust;
import com.cb.platform.yq.base.customsign.service.fileImage.AbstractLoadFileImage;

import java.io.File;

public class DefaultLoadFileImage extends AbstractLoadFileImage {

    /**
     * PDF 图片 操作目录的 绝对路径
     * @param loadFileImageReqeust
     * @return
     */
    @Override
    public String pdfImageAbsoluteOperatePath(LoadFileImageReqeust loadFileImageReqeust) {
       /* String path=PathUtils.getFilePath(PathUtils.FileVisitEnum.ABSOLUTE, PathUtils.FileTypeEnum.FILEIMAGE)+loadFileImageReqeust.getFileId();
        File file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return null;path+"/";*/
       return null;
    }


    /**
     * 虚拟路径转绝对路径
     * @param loadFileImageReqeust
     * @param filePath
     * @return
     */
    @Override
    public String absoluteTobrowserPath(LoadFileImageReqeust loadFileImageReqeust, String filePath) {
        return null;//PathUtils.changeFileVisit(PathUtils.FileVisitEnum.ABSOLUTE,PathUtils.FileVisitEnum.BROWSER,filePath);
    }
}
