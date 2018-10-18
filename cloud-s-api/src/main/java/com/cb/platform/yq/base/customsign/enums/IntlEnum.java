package com.cb.platform.yq.base.customsign.enums;

/**
 * 国际化枚举
 */
public enum IntlEnum {
    WORKFLOW_UPLOAD_SIGN_FILE_TYPE("WORKFLOW_UPLOAD_SIGN_FILE_TYPE","请上传[|]格式的文件,进行签名！"),
    WORKFLOW_MAIN_UPLOAD_FILE("WORKFLOW_MAIN_UPLOAD_FILE","文件名不能超过|个字符"),
    WORKFLOW_ACTION_UPLOAD_FILE_SIZE("WORKFLOW_ACTION_UPLOAD_FILE_SIZE","上传的文件不能超过"),
    WORKFLOW_AGAIN_UPLOAD_FILE("WORKFLOW_AGAIN_UPLOAD_FILE","请再次上传文件"),
    WORKFLOW_FILE_IS_CHANGE_ERROR("WORKFLOW_FILE_IS_CHANGE_ERROR","判断文件是否被篡改失败"),
    WORKFLOW_SIGN_CHECK_SM2_SIGN("WORKFLOW_SIGN_CHECK_SM2_SIGN","判断文件是否SM2签名失败"),
    WORKFLOW_SIGN_CHECK_RSA_SIGN("WORKFLOW_SIGN_CHECK_RSA_SIGN","判断文件是否RSA签名失败"),
    WORKFLOW_FILE_TO_PDF_FAIL("WORKFLOW_FILE_TO_PDF_FAIL","文件转PDF失败"),
    WORKFLOW_CUSTOMSIGN_VALIDPARAM_PDF_FILE_NO("WORKFLOW_CUSTOMSIGN_VALIDPARAM_PDF_FILE_NO","PDF文件不存在"),
    WORKFLOW_CUSTOMSIGN_VALIDPARAM_PDF_FILE_NO_PAGE("WORKFLOW_CUSTOMSIGN_VALIDPARAM_PDF_FILE_NO_PAGE","没有页码需要转换"),
    WORKFLOW_CUSTOMSIGN_VALIDPARAM_FILE_NO("WORKFLOW_CUSTOMSIGN_VALIDPARAM_FILE_NO","没有文件"),
    FILE_TO_PDF_FAIL("FILE_TO_PDF_FAIL","文件转图片失败"),
    UPLOAD_FILE_FAIL("UPLOAD_FILE_FAIL","上传失败"),
    WORKFLOW_PUBLIC_CER_EXIST_NO("WORKFLOW_PUBLIC_CER_EXIST_NO","请导入软证书！"),
    SIGN_FILE_FAIL("SIGN_FILE_FAIL","签名失败"),
    SIGN_FILE_HAS_RSA_SM2_SIGNED("SIGN_FILE_HAS_RSA_SM2_SIGNED","文件已添加过国密签名以及RSA签名，不可再添加国密签名"),
    INSERT_WATERMARK_FAIL("INSERT_WATERMARK_FAIL","插入水印失败"),
    SIGN_FILE_HAS_SIGNED("SIGN_FILE_HAS_SIGNED","文件已添加过数字签名，可再次添加数字签名，不可添加普通签名"),
    FILE_NO_EXISTS("FILE_NO_EXISTS","文件不存在"),
    FILE_NO_PDF_EXISTS("FILE_NO_PDF_EXISTS","文件不是PDF文件"),
    SIGN_GET_SOCKET_IP_PORT_FALI("SIGN_GET_SOCKET_IP_PORT_FALI","获取通信信息失败"),
    WORKFLOW_SREACH_FILE_NO_DATA("WORKFLOW_SREACH_FILE_NO_DATA","没有数据"),
    HAND_WRITE_UPLOAD_SUCESS("HAND_WRITE_UPLOAD_SUCESS","上传成功"),
    WORKFLOW_UPLOAD_IMAGE_EMPTY("WORKFLOW_UPLOAD_IMAGE_EMPTY","上传图片为空"),
    WORKFLOW_SAVE_IMAGE_FLAIE("WORKFLOW_SAVE_IMAGE_FLAIE","保存图片失败"),
    WORKFLOW_SIGN_PAGE_CLOSE("WORKFLOW_SIGN_PAGE_CLOSE","签名页面已经关闭"),
    WORKFLOW_UPLOAD_PAGE_CLOSE("WORKFLOW_UPLOAD_PAGE_CLOSE","上传页面已经关闭"),
    WORKFLOW_HAND_WRITE_SIGN_IMAGE_AGAIN("WORKFLOW_HAND_WRITE_SIGN_IMAGE_AGAIN","请不要重复上传图片"),
    WORKFLOW_READ_IMAGE_FLAIE("WORKFLOW_READ_IMAGE_FLAIE","读取图片失败"),
    WORKFLOW_HANDWRITESIGNACTION_FAILE("WORKFLOW_HANDWRITESIGNACTION_FAILE","请求失败"),
    WORKFLOW_SIGN_SESSION_FAIL("WORKFLOW_SIGN_SESSION_FAIL","签名会话丢失")
    ;


    private String flag;//标识
    private String zhcn;//中文
    //private String zhtw;
    //private String enus;

    private IntlEnum(String flag,String zhcn){
        this.flag=flag;
        this.zhcn=zhcn;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getZhcn() {
        return zhcn;
    }

    public void setZhcn(String zhcn) {
        this.zhcn = zhcn;
    }

    public String get(){
        return this.getZhcn();
    }
}
