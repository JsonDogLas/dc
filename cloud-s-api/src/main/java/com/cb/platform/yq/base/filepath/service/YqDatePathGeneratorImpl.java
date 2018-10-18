package com.cb.platform.yq.base.filepath.service;

import com.cb.platform.yq.base.filepath.service.impl.DatePathGeneratorImpl;

/**
 *云签 日期路径创建方式
 */
public class YqDatePathGeneratorImpl extends DatePathGeneratorImpl {
    public YqDatePathGeneratorImpl(String prefix, String id) {
        super(prefix, id);
    }

    @Override
    public String generatorPath() {
        return super.generatorPath()+this.getId()+"/";
    }
}
