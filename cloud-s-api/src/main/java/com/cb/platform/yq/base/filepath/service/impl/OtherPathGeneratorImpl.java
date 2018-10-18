package com.cb.platform.yq.base.filepath.service.impl;

import com.cb.platform.yq.base.filepath.service.PathGeneratorInterface;

/**
 * 其他路径生成器
 */
public class OtherPathGeneratorImpl implements PathGeneratorInterface {

    public OtherPathGeneratorImpl(String prefix) {
        this.prefix = prefix;
    }

    private String prefix;
    /**
     * 生成路径
     * @return
     */
    @Override
    public String generatorPath() {
        return prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
