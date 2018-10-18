package com.cb.platform.yq.base.filepath.service;

import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;

/**
 * 系统文件路径接口
 */
public interface SystemFilePathInterface {

    /**
     * 取得系统访问路径
     * @param fileVisitEnum
     * @param fileStoragePathInterface
     * @return
     */
    String getFilePath(FileVisitEnum fileVisitEnum, FilePathEnumInterface fileStoragePathInterface);

    /**
     * 切换文件访问方式
     * @param originalFileVisitEnum
     * @param goalFileVisitEnum
     * @param path
     * @return
     */
    String changeFileVisit(FileVisitEnum originalFileVisitEnum, FileVisitEnum goalFileVisitEnum, String path);
}
