package com.cb.platform.yq.base.filepath.service;

/**
 * 文件图片路径生成器
 */
public class FileImageGeneratorImpl implements PathGeneratorInterface {
    private String prefix;
    private String id;

    public FileImageGeneratorImpl(String prefix, String id) {
        this.prefix = prefix;
        this.id = id;
    }

    /**
     * 生成路径
     *
     * @return
     */
    @Override
    public String generatorPath() {
        return prefix+id+"/";
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
