package com.cb.platform.yq.base.permission.test;

/**
 * 权限类型
 */
public enum PermissionTypeEnum {
    MODULE("1","导航"),
    MENU("2","菜单（横竖菜单）"),
    BUTTON("3","按钮");
    public String flag;
    public String name;

    PermissionTypeEnum(String flag, String name) {
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
