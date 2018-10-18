package com.cb.producer.client;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultGenerateStorageClient;
import com.github.tobato.fastdfs.service.DefaultTrackerClient;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author jdl
 */
@Component
public class FastDfsClient {
    @Resource(name = "defaultGenerateStorageClient")
    DefaultGenerateStorageClient storageClient;

    @Autowired
    DefaultTrackerClient trackerClient;

    private static final String GROUP = "group1";

    /**
     * upload a local file
     * @param fullPath
     * @return
     */
    public String uploadFile(String fullPath) {
        File file = new File(fullPath);
        FileInputStream fis = null;
        try {
            fis = FileUtils.openInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String suffix = fullPath.substring(fullPath.lastIndexOf(".") + 1);
        StorePath storePath = storageClient.uploadFile(GROUP,fis,file.length(),suffix);
        return storePath.getFullPath();
    }

    /**
     * deal with a file-upload request
     * @param inputStream
     * @param fileSize
     * @param fileExtName
     * @return
     */
    public StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName) {
        return storageClient.uploadFile(GROUP,inputStream,fileSize,fileExtName);
    }

    public void deleteFile(String groupName,String path) {
        storageClient.deleteFile(groupName, path);
    }
}
