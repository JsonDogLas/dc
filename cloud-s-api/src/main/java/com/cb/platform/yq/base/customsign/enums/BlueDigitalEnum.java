package com.cb.platform.yq.base.customsign.enums;

/**
 * 蓝丝带效果
 */
public enum BlueDigitalEnum {
    ERROR("error","文件被破坏"),BLUE("blue","已经验证签过，不能在进行签名");
    private BlueDigitalEnum(String flag, String name){
        this.flag=flag;
        this.name=name;
    }
    private String flag;
    private String name;

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
