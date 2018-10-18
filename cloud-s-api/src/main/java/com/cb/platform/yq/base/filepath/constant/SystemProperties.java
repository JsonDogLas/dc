package com.cb.platform.yq.base.filepath.constant;


import com.ceba.base.utils.ftp.FtpObject;
import com.ceba.netty.server.config.NettyServerConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 云签API系统配置
 * 看你这么骚，我真的怕你不知道
 * @author whh
 */

@Component
@ConfigurationProperties(prefix = "com.cb.platform.yq.api",ignoreUnknownFields = true)
public class SystemProperties {

    /**
     * ftp配置
     */
    private FtpObject ftpObject;

    /**
     * 系统文件路径
     */
    private String systemFilePath;

    /**
     * 全局日志控制
     */
    private boolean logger;

    /**
     * 电子文件平台获取印章的接口
     */
    private String filePlatStampUrl;

    private String contextPaths;
    /**
     * 签名时Socket通信的IP
     */
    private String nettyServerId;

    /**
     * 签名时Socket通信的端口
     */
    private String nettyServerPort;
    /**
     * 签名时 蓝丝带 等级
     */
    private int certificateLevel;

    /**
     * 服务器端口号
     */
    @Value("${server.port}")
    private String serverPort;

    /**
     * 发起时上传文件大小 m
     */
    private int uploadFileSize;

    /**
     * 上传文件名称的字符限制
     */
    private int uploadFileNameSize;

    /**
     * 上传文件的类型限制
     */
    private String uploadFileType;
    /**
     * 核心线程数
     */
    private int corePoolSize;

    /**
     * 线程池所能容纳的最大线程数。超过这个数的线程将被主色
     */
    private int maximumPoolSize;

    /**
     * 非核心线程的闲置超时时间，超过这个时间就会被回收。
     */
    private long keepAliveTime;
    /**
     * 队列容量
     */
    private int capacity;

    /**
     * 签名上下文在内存中保留时间 单位分钟  活跃时间
     */
    private int signContextMemoryTime=15;

    /**
     * 默认保存30天 单位分钟  保留时间
     */
    private int signContextRedisTime=30*24*60;

    /**
     * 取得电子文件平台key信息的url
     */
    private String filePlatKeyInfoUrl;

    /**
     * 授权服务器的url
     */
    private String oauthUrl;
    //---------------------------------------------------------------------------------------
    /**
     * 是否打印日志
     */
    public static boolean loggerFalg=false;
    /**
     * 项目的访问
     */
    public static String contextPath="/";
    /**
     * 电子文件平台获取印章的接口
     */
    public static String _filePlatStampUrl="";
    /**
     * 取得电子文件平台key信息的url
     */
    public static String _filePlatKeyInfoUrl="";
    /**
     * 上传文件大小
     */
    public static int _uploadFileSize=180;
    /**
     * 上传文件的文件名称字数限制
     */
    public static int _uploadFileNameSize=255;
    /**
     * 上传文件的文件类型
     */
    public static String _uploadFileType="pdf|doc|docx|png|jpg";
    /**
     * 单位时间 秒 保留时间
     */
    public static int _signContextRedisTime=30 * 24 *3600;


    public String getOauthUrl() {
        return oauthUrl;
    }

    public void setOauthUrl(String oauthUrl) {
        this.oauthUrl = oauthUrl;
    }

    public String getContextPaths() {
        return contextPaths;
    }

    public void setContextPaths(String contextPaths) {
        SystemProperties.contextPath=contextPaths;
        this.contextPaths = contextPaths;
    }

    public FtpObject getFtpObject() {
        return ftpObject;
    }

    public void setFtpObject(FtpObject ftpObject) {
        this.ftpObject = ftpObject;
    }

    public String getSystemFilePath() {
        return systemFilePath;
    }

    public void setSystemFilePath(String systemFilePath) {
        SystemPathConstant.SYSTEM_FILE_PATH=systemFilePath;
        this.systemFilePath = systemFilePath;
    }

    public boolean isLogger() {
        return logger;
    }

    public void setLogger(boolean logger) {
        SystemProperties.loggerFalg=logger;
        this.logger = logger;
    }

    /**
     * 日志
     * @param logger
     */
    public static void log(Logger logger,String message){
        if(SystemProperties.loggerFalg){
            logger.info(message);
        }
    }


    public String getFilePlatStampUrl() {
        return filePlatStampUrl;
    }

    public void setFilePlatStampUrl(String filePlatStampUrl) {
        _filePlatStampUrl=filePlatStampUrl;
        this.filePlatStampUrl = filePlatStampUrl;
    }

    public String getNettyServerId() {
        return nettyServerId;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public void setNettyServerId(String nettyServerId) {
        NettyServerConstants.tcpIP = nettyServerId;

        this.nettyServerId = nettyServerId;
    }

    public String getNettyServerPort() {
        return nettyServerPort;
    }

    public void setNettyServerPort(String nettyServerPort) {
        NettyServerConstants.tcpPort = nettyServerPort;
        this.nettyServerPort = nettyServerPort;
    }

    public int getCertificateLevel() {
        return certificateLevel;
    }

    public void setCertificateLevel(int certificateLevel) {
        this.certificateLevel = certificateLevel;
    }


    public int getUploadFileSize() {
        return uploadFileSize;
    }

    public void setUploadFileSize(int uploadFileSize) {
        _uploadFileSize=uploadFileSize;
        this.uploadFileSize = uploadFileSize;
    }

    public int getUploadFileNameSize() {
        return uploadFileNameSize;
    }

    public void setUploadFileNameSize(int uploadFileNameSize) {
        _uploadFileNameSize=uploadFileNameSize;
        this.uploadFileNameSize = uploadFileNameSize;
    }

    public String getUploadFileType() {
        return uploadFileType;
    }

    public void setUploadFileType(String uploadFileType) {
        _uploadFileType=uploadFileType;
        this.uploadFileType = uploadFileType;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSignContextMemoryTime() {
        return signContextMemoryTime;
    }

    public long getSignContextMemoryMillisecond(){
        return signContextMemoryTime*60*1000;
    }
    public void setSignContextMemoryTime(int signContextMemoryTime) {
        if(signContextMemoryTime > 30 || signContextMemoryTime < 0){
            signContextMemoryTime=30;
        }
        this.signContextMemoryTime = signContextMemoryTime;
    }



    public int getSignContextRedisTime() {
        return signContextRedisTime;
    }

    public void setSignContextRedisTime(int signContextSaveTime) {
        _signContextRedisTime=signContextSaveTime;
        this.signContextRedisTime = signContextSaveTime;
    }

    public String getFilePlatKeyInfoUrl() {
        return filePlatKeyInfoUrl;
    }

    public void setFilePlatKeyInfoUrl(String filePlatKeyInfoUrl) {
        _filePlatKeyInfoUrl=filePlatKeyInfoUrl;
        this.filePlatKeyInfoUrl = filePlatKeyInfoUrl;
    }
}
