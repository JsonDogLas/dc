package com.cb.producer.entity;

import lombok.Data;

/**
 * @author jdl
 */
@Data
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
}
