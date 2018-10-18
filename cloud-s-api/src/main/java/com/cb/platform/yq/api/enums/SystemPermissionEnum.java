package com.cb.platform.yq.api.enums;

/**
 * 系统权限
 * @author whh
 */
public enum  SystemPermissionEnum {
    ROLE_DATE("ROLE_DATE","日期章"),
    ROLE_HAND_SIGN("ROLE_HAND_SIGN","手写签名"),
    ROLE_QRCODE("ROLE_QRCODE","二维码"),
    ROLE_TITLE("ROLE_TITLE","自定义标题"),
    ROLE_UPLOAD_STAMP("ROLE_UPLOAD_STAMP","上传印章"),
    ROLE_STAMP_INFO("ROLE_STAMP_INFO","获取签名信息接口是否有签章信息"),
    ROLE_NO_STAMP("ROLE_NO_STAMP","无签章签名");
    private String flag;
    private String name;

    SystemPermissionEnum(String flag, String name) {
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

    public static void main(String[] strs){
        StringBuffer stringBuffer=new StringBuffer();
        for(SystemPermissionEnum systemPermissionEnum:SystemPermissionEnum.values()){
            stringBuffer.append(systemPermissionEnum.flag);
            stringBuffer.append(",");
        }
        System.out.println(stringBuffer.toString());
    }
}
