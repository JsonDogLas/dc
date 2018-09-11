package com.cb.producer.entity;
/**
 * @author jdl
 */
public class FileUploadRes {
    public Integer result;
    public String path;
    public String message;

    public FileUploadRes() {

    }

    public FileUploadRes(Integer result, String path, String message) {
        this.result = result;
        this.path = path;
        this.message = message;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
