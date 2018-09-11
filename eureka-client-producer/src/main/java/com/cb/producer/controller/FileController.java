package com.cb.producer.controller;

import com.cb.producer.entity.FileUploadReq;
import com.cb.producer.entity.FileUploadRes;
import com.cb.producer.entity.UrlMap;
import com.cb.producer.client.FastDfsClient;
import com.cb.producer.dao.UrlMapRepository;
import com.cb.producer.util.Md5Util;
import com.github.tobato.fastdfs.domain.StorePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
/**
 * @author jdl
 */
@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    FastDfsClient fastDfsClient;

    @Autowired
    UrlMapRepository urlMapRepository;

    @Value("${nginx.resource.addr}")
    String host;

    private static final String SUCCESS = "success";

    private static final String EXISTED = "existed";

    private static final String ERROR = "error";

    public Boolean isExist(String fileName) {
        return urlMapRepository.existsByFileName(fileName);
    }

    @PostMapping("/upload")
    public FileUploadRes upload(@RequestParam("file") MultipartFile file) {
        Integer result = 0;
        String message = "";
        String path = host + "/";
        try {
            String fullName = file.getOriginalFilename();
            if(isExist(fullName)) {
                UrlMap map = urlMapRepository.findByFileName(fullName);
                path = path + map.getGroupName() + "/" + map.getPath();
                message = EXISTED;
            } else {
                String suffix = fullName.substring(fullName.lastIndexOf(".") + 1);
                StorePath storePath = fastDfsClient.uploadFile(file.getInputStream(),file.getSize(),suffix);
                path = path + storePath.getFullPath();
                UrlMap urlMap = new UrlMap(fullName, storePath.getGroup(), storePath.getPath());
                urlMapRepository.save(urlMap);
                message = SUCCESS;
            }
        } catch (Exception e) {
            message = e.getMessage();
            logger.error("file upload error",e);
        }
        if(!SUCCESS.equals(message) && !EXISTED.equals(message)) {
            path = "";
            result = -1;
        }
        return new FileUploadRes(result,path,message);
    }

    @PostMapping("/upload/string")
    public FileUploadRes uploadByString(@RequestBody FileUploadReq req) {
        Integer result = 0;
        String message = "";
        String path = host + "/";
        try {
            String fullName = req.getFileName();
            if(isExist(fullName)) {
                UrlMap map = urlMapRepository.findByFileName(fullName);
                path = path + map.getGroupName() + "/" + map.getPath();
                message = EXISTED;
            } else {
                //取后缀
                String suffix = "";
                int indexSuffix = fullName.lastIndexOf(".");
                if(indexSuffix != -1) {
                    suffix = fullName.substring(indexSuffix + 1);
                }
                //BASE64解码
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] fileBinary = decoder.decodeBuffer(req.getFileData());
                InputStream is = new ByteArrayInputStream(fileBinary);
                //上传到fdfs
                StorePath storePath = fastDfsClient.uploadFile(is,fileBinary.length,suffix);
                path = path + storePath.getFullPath();
                UrlMap urlMap = new UrlMap(fullName, storePath.getGroup(), storePath.getPath());
                urlMapRepository.save(urlMap);
                message = SUCCESS;
            }
        } catch (Exception e) {
            message = e.getMessage();
            logger.error("file upload error",e);
        }
        if(!SUCCESS.equals(message) && !EXISTED.equals(message)) {
            path = "";
            result = -1;
        }
        return new FileUploadRes(result,path,message);
    }

    @GetMapping("/files")
    public List<UrlMap> getFileList() {
        return urlMapRepository.findAll();
    }

    @DeleteMapping("/delete/all")
    public String deleteAll() {
        List<UrlMap> maps = urlMapRepository.findAll();
        for (UrlMap map : maps) {
            fastDfsClient.deleteFile(map.getGroupName(),map.getPath());
        }
        urlMapRepository.deleteAll();
        return "files";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") Long id) {
        Optional op = urlMapRepository.findById(id);
        UrlMap map = (UrlMap) op.get();
        if(map != null) {
            fastDfsClient.deleteFile(map.getGroupName(),map.getPath());
            urlMapRepository.deleteById(id);
        }
        return "files";
    }

    @DeleteMapping("/delete/name/{fileName}")
    public String deleteFileByName(@PathVariable("fileName") String fileName) {
        UrlMap map = urlMapRepository.findByFileName(fileName);
        if(map != null) {
            fastDfsClient.deleteFile(map.getGroupName(),map.getPath());
            urlMapRepository.deleteById(map.getId());
        }
        return SUCCESS;
    }
}
