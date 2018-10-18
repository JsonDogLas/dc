package com.cb.platform.yq.base.filepath.enums;


import com.cb.platform.yq.base.filepath.constant.SystemPathConstant;
import com.ceba.base.utils.ftp.FtpConfig;

/**
 * 文件访问方式
 */
public enum  FileVisitEnum {
    BROWSER("browser","浏览器路径--用于浏览器访问", "/"+ SystemPathConstant.FILE_FILTER_PATH),
    FTP_BROWSER("ftp_browser","ftp路径--通过浏览器访问", FtpConfig.getFtpObject().getFileUrl()),
    FTP_VIRTUAL("ftp_virtual","ftp虚拟路径--用于数据库存储",""),
    VIRTUAL("virtual","虚拟路径--用于数据库存储",""),
    ABSOLUTE("absolute","绝对路径--用于文件IO",SystemPathConstant.SYSTEM_FILE_PATH);
    FileVisitEnum(String flag, String name,String path){
        this.flag=flag;
        this.name=name;
        this.path=path;
    }
    private String name;
    private String flag;
    private String path;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
