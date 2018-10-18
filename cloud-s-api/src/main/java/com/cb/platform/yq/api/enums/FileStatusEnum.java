package com.cb.platform.yq.api.enums;

/**
 * 文件状态
 */
public enum FileStatusEnum {
    SHOW("1","显示"),
    DELETE("2","删除");
    private String flag;
    private String name;

    FileStatusEnum(String flag, String name) {
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
