package com.cb.platform.yq.base.filepath.service.impl;


import com.cb.platform.yq.base.filepath.service.PathGeneratorInterface;
import com.ceba.base.utils.IDSDateUtils;

/**
 * 日期路径生成器-以当前系统时间为准
 */
public class DatePathGeneratorImpl implements PathGeneratorInterface {
    private String prefix;
    private String id;

    public DatePathGeneratorImpl(String prefix, String id) {
        this.prefix = prefix;
        this.id = id;
    }
    /**
     * 生成路径
     * @return
     */
    @Override
    public String generatorPath() {
        return prefix+ IDSDateUtils.getYear()+"/"+ IDSDateUtils.getMonth()+"/"+IDSDateUtils.getDay()+"/";
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
