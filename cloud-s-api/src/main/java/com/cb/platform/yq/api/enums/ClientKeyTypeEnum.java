package com.cb.platform.yq.api.enums;

/**
 * 客户端key
 *
 * * 1。电子文件平台印章  标识  FILE_PLAT_STAMP
 * 2。第三方提供        标识  CLIENT_PLAT_STAMP
 */
public enum  ClientKeyTypeEnum {
    FILE_PLAT_STAMP("FILE_PLAT_STAMP","电子文件平台印章"),
    CLIENT_PLAT_STAMP("CLIENT_PLAT_STAMP","第三方提供");

    private String flag;
    private String name;

    ClientKeyTypeEnum(String flag, String name) {
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
