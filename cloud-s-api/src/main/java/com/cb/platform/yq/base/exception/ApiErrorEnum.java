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
public enum ApiErrorEnum implements ErrorInfoInterface {
    CLIENT_INFO_BY_APPID_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"0","CLIENT_INFO_BY_APPID_ERROR","客户端信息查询失败"),
    CE_INDEX_CEBA_KEY_EXCEPTION(ApiNumberEnum.YQ_API.getExceptionNumber()+"1","CE_INDEX_CEBA_KEY_EXCEPTION","判断测吧key异常"),
    SYSTEM_FILE_PATH_NO_FILE_EXCEPTION(ApiNumberEnum.YQ_API.getExceptionNumber()+"2","SYSTEM_FILE_PATH_NO_FILE_EXCEPTION","系统文件路径没有发现异常"),
    SYSTEM_FILE_IO_EXCEPTION(ApiNumberEnum.YQ_API.getExceptionNumber()+"3","SYSTEM_FILE_IO_EXCEPTION","上传文件签名IO发生了异常"),
    SIGN_LOAD_DATA_THREAD_EXCEPTION(ApiNumberEnum.YQ_API.getExceptionNumber()+"4","SIGN_LOAD_DATA_THREAD_EXCEPTION","异步加载签名数据异常"),
    CREATE_API_USER_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"5","CREATE_API_USER_ERROR","创建用户错误"),
    CREATE_FILE_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"6","CREATE_FILE_ERROR","不能创建文件"),
    READER_FILE_IO_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"7","READER_FILE_IO_ERROR","读文件流出错"),
    NO_URL_EXCEPTION(ApiNumberEnum.YQ_API.getExceptionNumber()+"8","NO_URL_EXCEPTION","第三方加载没有接口"),
    THIRD_PARTY_STAMP_EXCEPTION(ApiNumberEnum.YQ_API.getExceptionNumber()+"9","THIRD_PARTY_STAMP_EXCEPTION","第三方印章加载出错"),
    THIRD_STAMP_PARAM_EXCEPTION(ApiNumberEnum.YQ_API.getExceptionNumber()+"10","THIRD_STAMP_PARAM_EXCEPTION","印章参数不对"),
    THIRD_STAMP_TYPE_EXCEPTION(ApiNumberEnum.YQ_API.getExceptionNumber()+"11","THIRD_STAMP_TYPE_EXCEPTION","印章类型不对"),
    THIRD_LOAD_JSON_EXCEPTION(ApiNumberEnum.YQ_API.getExceptionNumber()+"12","THIRD_LOAD_JSON_EXCEPTION","URL编码异常"),
    CST_INSERT_SIGN_FILE_LOG_FAIL(ApiNumberEnum.YQ_API.getTipNumber()+"13","CST_INSERT_SIGN_FILE_LOG_FAIL","插入签名日志失败"),
    SIGN_DATA_FAIL(ApiNumberEnum.YQ_API.getTipNumber()+"14","SIGN_DATA_FAIL","异步处理数据报错"),
    CREATE_QUARTZ_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"15","CREATE_QUARTZ_ERROR","清除签名上下文内存报错"),
    CREATE_RUN_STAMP_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"16","CREATE_RUN_STAMP_ERROR","异步取印章报错"),
    UPLOAD_STAMP_FAIL(ApiNumberEnum.YQ_API.getExceptionNumber()+"17","UPLOAD_STAMP_FAIL","上传印章失败"),
    UPLOAD_STAMP_NO_File_FAIL(ApiNumberEnum.YQ_API.getExceptionNumber()+"18","UPLOAD_STAMP_NO_File_FAIL","上传印章文件不存在"),
    CE_INDEX_CEBA_KEY_FAIL2(ApiNumberEnum.YQ_API.getExceptionNumber()+"19","CE_INDEX_CEBA_KEY_FAIL","判断测吧key异常"),
    CE_TRANSPARENT_STAMP_FAIL(ApiNumberEnum.YQ_API.getExceptionNumber()+"20","CE_TRANSPARENT_STAMP_FAIL","创建透明章错误"),
    CE_SIGN_LOG_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"21","CE_SIGN_LOG_ERROR","插入签名日志错误"),
    CE_SIGN_LOG_QUERY(ApiNumberEnum.YQ_API.getExceptionNumber()+"22","CE_SIGN_LOG_QUERY","查询文件错误"),
    CE_SIGN_USER_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"23","CE_SIGN_USER_ERROR","创建签名用户错误"),
    QUERY_SIGN_USER_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"24","QUERY_SIGN_USER_ERROR","查询签名用户错误"),
    CE_INDEX_CEBA_KEY_ERROR(ApiNumberEnum.YQ_API.getExceptionNumber()+"25","CE_INDEX_CEBA_KEY_ERROR","请使用上海测吧信息技术有限公司发行的key"),
    CLIENT_INFO_BY_APPID_ERROR3(ApiNumberEnum.YQ_API.getExceptionNumber()+"0","CLIENT_INFO_BY_APPID_ERROR","客户端信息查询失败");

    //状态码
    private String code;

    //资源文件对应信息的资源码
    private String resourceCode;

    //中文信息
    private String message;

    ApiErrorEnum(String code, String resourceCode, String message) {
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
