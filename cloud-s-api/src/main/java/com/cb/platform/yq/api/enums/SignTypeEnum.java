package com.cb.platform.yq.api.enums;

/**
 * 签名类型
 * 表名 yq_sign_file_log
 * 属性 SIGN_TYPE
 */
public enum SignTypeEnum {
    REVISE_SIGN("1","修订签"),
    VALID_SIGN("2","验证签"),
    ORDINARY_SIGN("3","普通签");

    private String flag;
    private String name;

    SignTypeEnum(String flag, String name) {
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
