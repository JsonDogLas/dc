package com.cb.platform.yq.base.permission.test;

/**
 * 权限编号 创建规则
 */
public interface CodeGenerateInterface {

    /**
     * 当前权限code
     * @return
     */
    String code();

    /**
     * 父节点ID
     * @return
     */
    String pid();

    /**
     * id
     * @return
     */
    String id();

    /**
     * 应用ID
     * @return
     */
    String applicationId();


    CodeGenerateInterface getCodeGenerateInterface();

}
