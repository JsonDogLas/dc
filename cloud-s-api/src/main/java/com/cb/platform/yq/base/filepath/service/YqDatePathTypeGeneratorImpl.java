package com.cb.platform.yq.base.filepath.service;


import com.cb.platform.yq.base.filepath.service.impl.DatePathGeneratorImpl;

/**
 * 数字申请--》申请资料 目录生成器
 */
public class YqDatePathTypeGeneratorImpl extends DatePathGeneratorImpl {
    private String type;
    public YqDatePathTypeGeneratorImpl(String prefix, String id, String type) {
        super(prefix, id);
        this.type=type;
    }

    @Override
    public String generatorPath() {
        return super.generatorPath()+this.type+"/"+this.getId()+"/";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
