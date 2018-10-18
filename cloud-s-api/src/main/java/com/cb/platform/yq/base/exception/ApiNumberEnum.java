package com.cb.platform.yq.base.exception;

import com.cb.platform.yq.base.log.ModulesNumberInterface;

/**
 * 平台入口编号枚举
 */
public enum ApiNumberEnum implements ModulesNumberInterface {
    YQ_API("1","云签接口"),
    CLIENT_MANAGER("2","客户单信息管理");
    private String number;
    private String name;

    private ApiNumberEnum(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 取得前端点击编号
     *
     * @return
     */
    @Override
    public String getWebNumber() {
        return getRoot().getWebNumber()+number;
    }

    /**
     * 取得service编号
     *
     * @return
     */
    @Override
    public String getServiceNumber() {
        return getRoot().getServiceNumber()+number;
    }

    /**
     * 取得异常编号
     *
     * @return
     */
    @Override
    public String getExceptionNumber() {
        return getRoot().getExceptionNumber()+number;
    }

    /**
     * 取得提示的编号
     *
     * @return
     */
    @Override
    public String getTipNumber() {
        return getRoot().getTipNumber()+number;
    }

    /**
     * 取得根节点
     * @return
     */
    @Override
    public ModulesNumberInterface getRoot() {
        return SystemRootNumberEnum.YQ_ROOT;
    }



}
