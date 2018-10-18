package com.cb.platform.yq.api.enums;

/**
 * 文件状态
 */
public enum FileTypeEnum {
    API_TEMP("9","API系统缓存文件"),
    API_FTP("8","API系统ftp文件"),
    API_STAMP("7","API签章文件");
    private String flag;
    private String name;

    FileTypeEnum(String flag, String name) {
        this.flag = flag;
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
