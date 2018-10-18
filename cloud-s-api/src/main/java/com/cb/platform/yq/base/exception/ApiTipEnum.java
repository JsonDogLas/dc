package com.cb.platform.yq.base.exception;

import com.ceba.base.exception.enums.ErrorInfoInterface;

/**
 * 首页请求类业务代码
 * ME_模块名称_意思
 * ME = MANAGER_ERROR
 * --41000
 *    41001
 *      41001_0
 *    41002
 *      41002_0
 * @author whh
 */
public enum ApiTipEnum implements ErrorInfoInterface {
    CLIENT_INFO_BY_APPID_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"0","CLIENT_INFO_BY_APPID_ERROR","客户端信息查询失败"),
    CE_INDEX_CEBA_KEY_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"2","CE_INDEX_CEBA_KEY_ERROR","请使用上海测吧信息技术有限公司发行的key"),
    READ_FILE_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"3","READ_FILE_ERROR","上传文件签名，读取文件失败"),
    SYSTEM_PARAM_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"4","SYSTEM_PARAM_ERROR","系统参数错误"),
    SIGN_FILD_NO_FIND(ApiNumberEnum.YQ_API.getExceptionNumber()+"5","SIGN_FILD_NO_FIND","没有发现文件，请重新上传"),
    KEY_INFO_NO_FIND(ApiNumberEnum.YQ_API.getExceptionNumber()+"6","SIGN_FILD_NO_FIND","没有发现key身份信息，请重新上传"),
    KEY_ID_NO_FIND(ApiNumberEnum.YQ_API.getExceptionNumber()+"7","KEY_ID_NO_FIND","没有发现keyId信息，请重新上传"),
    OPEN_SIGN_PAGE_OUT_TIME(ApiNumberEnum.YQ_API.getExceptionNumber()+"8","SIGN_OUT_TIME","打开签名页面超时，请重新上传文件"),
    LOAD_SIGN_DATA_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"9","LOAD_SIGN_DATA_ERROR","加载签名数据失败，请重新上传文件"),
    CREATE_API_USER_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"10","CREATE_API_USER_ERROR","创建用户错误"),
    ADD_SIGN_FILE_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"11","ADD_SIGN_FILE_ERROR","添加签名文件错误"),
    ME_THIRD_GET_FAIL(ApiNumberEnum.YQ_API.getExceptionNumber()+"12","ME_THIRD_GET_FAIL","系统繁忙，请稍后再试"),
    ME_UPLOAD_STAMP_FAIL(ApiNumberEnum.YQ_API.getExceptionNumber()+"13","ME_THIRD_GET_FAIL","没有上传印章的权限"),
    WORKFLOW_SIGN_SESSION_FAIL(ApiNumberEnum.YQ_API.getExceptionNumber()+"14","WORKFLOW_SIGN_SESSION_FAIL","签名会话丢失"),
    WORKFLOW_NO_FILE_FAIL(ApiNumberEnum.YQ_API.getExceptionNumber()+"15","WORKFLOW_NO_FILE_FAIL","访问的文件不存在"),
    CREATE_RUN_STAMP_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"16","CREATE_RUN_STAMP_ERROR","异步取印章报错"),
    CREATE_RUN_STAMP_PNG_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"17","CREATE_RUN_STAMP_PNG_ERROR","印章图片名称必须是png"),
    UPLOAD_STAMP_NULL(ApiNumberEnum.YQ_API.getExceptionNumber()+"18","UPLOAD_STAMP_NULL","上传印章不能为空"),
    UPLOAD_STAMP_SUCCESS(ApiNumberEnum.YQ_API.getExceptionNumber()+"19","UPLOAD_STAMP_SUCCESS","上传印章成功"),
    CLIENT_INFO_BY_APPID_ERROR2(ApiNumberEnum.YQ_API.getExceptionNumber()+"0","CLIENT_INFO_BY_APPID_ERROR","客户端信息查询失败"),
    CLIENT_INFO_BY_APPID_ERROR3(ApiNumberEnum.YQ_API.getExceptionNumber()+"0","CLIENT_INFO_BY_APPID_ERROR","客户端信息查询失败");


    //状态码
    private String code;

    //资源文件对应信息的资源码
    private String resourceCode;

    //中文信息
    private String message;

    ApiTipEnum(String code, String resourceCode, String message) {
        this.code = code;
        this.resourceCode = resourceCode;
        this.message = message;
    }
    public String getCode(){
        return this.code;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public String getMessage(){
        return this.message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
