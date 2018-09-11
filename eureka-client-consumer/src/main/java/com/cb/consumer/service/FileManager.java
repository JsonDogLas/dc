package com.cb.consumer.service;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author jdl
 */
@FeignClient(value = "file-service", configuration = FileManager.MultipartSupportConfig.class)
public interface FileManager {

    /**
     * call a upload-file micro-service
     * @param file
     * @return JSON String
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadFile(@RequestPart("file") MultipartFile file);

    /**
     * upload by string
     * @param map
     * @return
     */
    @RequestMapping(value = "/upload/string", method = RequestMethod.POST)
    String uploadFileByString(@RequestParam Map<String,Object> map);

    /**
     * get files list
     * @return JSON String
     */
    @GetMapping("/files")
    String getFiles();

    /**
     * delete all storing files
     * @return "files"
     */
    @DeleteMapping("/delete/all")
    String deleteAll();

    /**
     * delete file by id
     * @param id
     * @return "files"
     */
    @DeleteMapping("/delete/{id}")
    String deleteFile(@PathVariable("id") Long id);

    class MultipartSupportConfig {

        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }
}
