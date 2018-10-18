package com.cb.platform.yq.base.customsign.entity;

import com.cb.platform.yq.base.customsign.enums.BlueDigitalEnum;

/**
 * 签名文件新信息
 */
public class SignFileInfo {
    private String virtualPath;//文件虚拟路径 (pdf文件)
    private String filePath;//文件绝对路径 (pdf文件)
    private String originalPath;//原文件绝对路径(原文件绝对路径)--》word,png,pdf
    private String fileName;//文件名
    private int totalPageCount;//文件总页数
    private boolean hasSm2Signed;//是否SM2签名
    private boolean hasRSASigned;//是否RSA签名
    private BlueDigitalEnum blueDigital;//蓝丝带效果
    private String fileId;//文件ID


    public String getFileId() {

        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVirtualPath() {
        return virtualPath;
    }

    public void setVirtualPath(String virtualPath) {
        this.virtualPath = virtualPath;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public boolean getHasSm2Signed() {
        return hasSm2Signed;
    }

    public void setHasSm2Signed(boolean hasSm2Signed) {
        this.hasSm2Signed = hasSm2Signed;
    }

    public boolean getHasRSASigned() {
        return hasRSASigned;
    }

    public void setHasRSASigned(boolean hasRSASigned) {
        this.hasRSASigned = hasRSASigned;
    }

    public BlueDigitalEnum getBlueDigital() {
        return blueDigital;
    }

    public String getBlueDigitalStr(){
        if(blueDigital == null){
            return null;
        }
        return blueDigital.getFlag();
    }

    public void setBlueDigital(BlueDigitalEnum blueDigital) {
        this.blueDigital = blueDigital;
    }

    /**
     * 取得文件后缀
     * @return
     */
    public String getFileExt(){
        return fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
    }

    public String getFilePathFileExt(){
        return filePath.substring(filePath.lastIndexOf(".")+1,filePath.length());
    }

    public String getTitle(){
        int index=fileName.lastIndexOf(".");
        if(index >= 0){
            return fileName.substring(0,index);
        }
        return null;
    }

    public String getFileNameStr(){
        if(fileName.length() > 12){
            return fileName.substring(0,12)+"...";
        }
        return fileName;
    }
}
