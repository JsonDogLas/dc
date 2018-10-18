package com.cb.platform.yq.base.customsign.service;

import com.alibaba.druid.util.StringUtils;
import com.cb.platform.yq.api.utils.ID;
import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.enums.YqFilePathEnum;
import com.cb.platform.yq.base.filepath.service.YqSystemFilePathImpl;

public class DefaultLoadSignData extends AbstractLoadSignData {
    /**
     * 根据文件名取得原文件路径
     * @return
     */
    @Override
    public String originalFilePath(String fileName) {
        String path= YqFilePathEnum.LOCAL_SERVER_ROOT.getFilePath(FileVisitEnum.ABSOLUTE, ID.getGuid());
        return path+fileName;
    }
    /**
     * PDF操作目录
     * @return
     */
    @Override
    public String pdfFilePath(String fileName) {
        fileName= fileName.substring(0,(fileName.lastIndexOf(".")+1));
        String path= YqFilePathEnum.LOCAL_SERVER_ROOT.getFilePath(FileVisitEnum.ABSOLUTE,ID.getGuid());
        return path+fileName+PDF_EXT;
    }
    /**
     * 绝对路径转虚拟路径
     * @param absolutePath
     * @return
     */
    @Override
    public String absolutePathToVirtualPath(String absolutePath) {
        return YqSystemFilePathImpl.getUpfileFilePath().changeFileVisit(FileVisitEnum.ABSOLUTE, FileVisitEnum.VIRTUAL,absolutePath);
    }
}
