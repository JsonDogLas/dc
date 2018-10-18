package com.cb.platform.yq.api.sign.bean.impl;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 适配FileItem
 * @author whh
 */
public class FileItemAdapter implements FileItem {

    private MultipartFile multiUploadFile;

    public FileItemAdapter(MultipartFile multiUploadFile) {
        this.multiUploadFile = multiUploadFile;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return multiUploadFile.getInputStream();
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getName() {
        return multiUploadFile.getOriginalFilename();
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public long getSize() {
        return multiUploadFile.getSize();
    }

    @Override
    public byte[] get() {
        return new byte[0];
    }

    @Override
    public String getString(String s) throws UnsupportedEncodingException {
        return null;
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public void write(File file) throws Exception {

    }

    @Override
    public void delete() {

    }

    @Override
    public String getFieldName() {
        return null;
    }

    @Override
    public void setFieldName(String s) {

    }

    @Override
    public boolean isFormField() {
        return false;
    }

    @Override
    public void setFormField(boolean b) {

    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public FileItemHeaders getHeaders() {
        return null;
    }

    @Override
    public void setHeaders(FileItemHeaders fileItemHeaders) {

    }
}
