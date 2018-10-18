package com.cb.platform.yq.base.interfaces.enums;

public enum RoleStatus {



    ROLE_STATUS_NORMAL("1","正常"),//正常
    ROLE_STATUS_DElETE("-1","删除"),//删除
    ROLE_STATUS_LOCK("2","锁定"),//锁定
    ROLE_STATUS_UNACTIVE("3","未激活");//未激活


    RoleStatus (String type,String name) {
        this.type=type;
        this.name=name;
    }
    private String type;
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
