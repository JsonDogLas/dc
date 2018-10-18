package com.cb.producer.entity;

import lombok.*;

/**
 * @author jdl
 */
@Data
public class FileUploadReq {
    public String signId;
    public String fileData;
    public String fileName;

}
