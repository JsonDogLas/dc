package com.cb.platform.yq.base.filepath.constant;

/**
 * 云签路径常量
 */
public class YqPathConstant {
    public final static String LOCAL="yq";
    public final static String FTP="fs";
    public final static String TEMP="temp";
    public final static String FILE_IMAGE="imagefile";
    public final static String SZ="sz";
    public final static String SQ="sq";
    public final static String YZ="yz";

    public final static String QRCODE_FILTER="/qrcodeVisit";
    public static String LOCAL_SERVER_ROOT=LOCAL+"/"+TEMP+"/";//服务器本地缓存目录
    public static String FTP_SERVER_ROOT=LOCAL+"/"+FTP+"/";//FTP服务器目录
    public static String LOCAL_SERVER_FILE_IMAGE=LOCAL_SERVER_ROOT+FILE_IMAGE+"/";//本地服务器文件图片
    public static String SQ_ROOT=SZ+"/"+SQ+"/";//申请资料根目录

}
