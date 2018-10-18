package com.cb.producer.controller;

import com.cb.producer.entity.FileUploadReq;
import com.cb.producer.entity.FileUploadRes;
import com.cb.producer.entity.FileUrlMap;
import com.cb.producer.client.FastDfsClient;
import com.cb.producer.dao.FileUrlMapRepository;
import com.cb.producer.util.IdUtil;
import com.cb.producer.util.StringUtil;
import com.github.tobato.fastdfs.domain.StorePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author jdl
 */
@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    FastDfsClient fastDfsClient;

    @Autowired
    FileUrlMapRepository fileUrlMapRepository;

    @Value("${nginx.resource.addr}")
    String host;

    private static final int RESULT_SUCCESS = 0;

    private static final int RESULT_FAILURE = -1;

    private static final String RESULT_MESSAGE_SUCCESS = "success";

    private static final String RESULT_MESSAGE_EXISTED = "existed";

    /**
     * 以表单提交的方式上传文件
     * @param file 文件表单
     * @return FileUploadRes
     */
    @PostMapping("/upload")
    public FileUploadRes upload(@RequestParam("file") MultipartFile file, @RequestParam(name = "signId",required = false) String signId) {
        String prefix = host + "/";
        try {
            Optional<String> idOp = Optional.ofNullable(signId);
            Optional<String> pathOp = idOp.flatMap(id -> fileUrlMapRepository.findById(id).map(FileUrlMap::getGroupPath).map(e -> prefix + e));
            if(pathOp.isPresent()) {
                return new FileUploadRes(RESULT_SUCCESS,pathOp.get(),RESULT_MESSAGE_EXISTED);
            }
            signId = idOp.orElse(IdUtil.getRandomIdByUUID());
            String fileName = file.getOriginalFilename();
            String suffix = StringUtil.getSuffix(fileName);
            StorePath storePath = fastDfsClient.uploadFile(file.getInputStream(),file.getSize(),suffix);
            FileUrlMap fileUrlMap = new FileUrlMap(signId,fileName, storePath.getGroup(), storePath.getPath());
            fileUrlMapRepository.save(fileUrlMap);
            return new FileUploadRes(RESULT_SUCCESS, prefix + storePath.getFullPath(), RESULT_MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("file upload error",e);
            return new FileUploadRes(RESULT_FAILURE, "", e.getMessage());
        }
    }

    @GetMapping("/uri/{id}")
    public String getUriById(@PathVariable("id") String id) {
        return fileUrlMapRepository.findById(id).map(e -> host + "/" + e.getGroupPath()).orElse("");
    }

    /**
     * 以BASE64编码方式上传文件
     * @param req 请求参数
     * @return FileUploadRes
     */
    @PostMapping("/upload/string")
    public FileUploadRes uploadByString(@RequestBody FileUploadReq req) {
        String prefix = host + "/";
        try {
            Optional<String> idOp = Optional.ofNullable(req.getSignId());
            Optional<String> pathOp = idOp.flatMap(id -> fileUrlMapRepository.findById(id).map(FileUrlMap::getGroupPath).map(e -> prefix + e));
            if(pathOp.isPresent()) {
                return new FileUploadRes(RESULT_SUCCESS,pathOp.get(),RESULT_MESSAGE_EXISTED);
            }
            String signId = idOp.orElse(IdUtil.getRandomIdByUUID());
            //不存在或未传id值,上传
            //取后缀
            String fileName = req.getFileName();
            String suffix = StringUtil.getSuffix(fileName);
            //BASE64解码
            byte[] fileBinary = new BASE64Decoder().decodeBuffer(req.getFileData());
            InputStream is = new ByteArrayInputStream(fileBinary);
            //上传到fdfs
            StorePath storePath = fastDfsClient.uploadFile(is,fileBinary.length,suffix);
            //上传记录保存
            fileUrlMapRepository.save(new FileUrlMap(signId, fileName, storePath.getGroup(), storePath.getPath()));
            return new FileUploadRes(RESULT_SUCCESS,prefix + storePath.getFullPath(),RESULT_MESSAGE_SUCCESS);
        } catch (Exception e) {
            logger.error("file upload error",e);
            return new FileUploadRes(RESULT_FAILURE,"",e.getMessage());
        }
    }

    @GetMapping("/files")
    public List<FileUrlMap> getFileList() {
        return fileUrlMapRepository.findAll();
    }

    @DeleteMapping("/delete/all")
    public String deleteAll() {
        List<FileUrlMap> maps = fileUrlMapRepository.findAll();
        maps.forEach(map -> fastDfsClient.deleteFile(map.getGroupName(),map.getPath()));
        fileUrlMapRepository.deleteAll();
        return RESULT_MESSAGE_SUCCESS;
    }

    private Consumer<Optional<FileUrlMap>> delConsumer = e -> e.ifPresent(f -> {
        fastDfsClient.deleteFile(f.getGroupName(),f.getPath());
        fileUrlMapRepository.deleteById(f.getId());});

    @DeleteMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") String id) {
        delConsumer.accept(fileUrlMapRepository.findById(id));
        return RESULT_MESSAGE_SUCCESS;
    }

    @DeleteMapping("/delete/name/{fileName}")
    public String deleteFileByName(@PathVariable("fileName") String fileName) {
        delConsumer.accept(Optional.ofNullable(fileUrlMapRepository.findByFileName(fileName)));
        return RESULT_MESSAGE_SUCCESS;
    }

}
