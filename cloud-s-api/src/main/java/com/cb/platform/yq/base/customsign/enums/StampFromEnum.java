package com.cb.platform.yq.base.customsign.enums;

/**
 * 印章来源
 *
 * 印章来源 1页面上传 2第三方 3电子文件平台
 */
public enum StampFromEnum {
    API_PLAT("1","云签API"),
    THREE_PLAT("2","第三方"),
    FILE_PLAT("3","电子文件平台");
    StampFromEnum(String number, String name) {
        this.number=number;
        this.name=name;
    }
    private String number;
    private String name;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
