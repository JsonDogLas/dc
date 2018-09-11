package com.cb.producer.entity;

/**
 * @author jdl
 */
public class FileUploadReq {
    public String fileData;
    public String fileName;

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
