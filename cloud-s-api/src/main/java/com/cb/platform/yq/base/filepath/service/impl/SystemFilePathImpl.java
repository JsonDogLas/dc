package com.cb.platform.yq.base.filepath.service.impl;

import com.cb.platform.yq.base.filepath.enums.FileVisitEnum;
import com.cb.platform.yq.base.filepath.service.AbstractSystemFilePath;

public class SystemFilePathImpl extends AbstractSystemFilePath {
    /**
     * 路径前缀特殊处理
     *
     * @param fileVisitEnum
     * @return
     */
    @Override
    public String pathPrefix(FileVisitEnum fileVisitEnum) {
        return null;
    }
}
