package com.cb.consumer.client;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jdl
 */
@FeignClient(value = "file-service", configuration = FileManagerClient.MultipartSupportConfig.class)
public interface FileManagerClient {

    /**
     * call a upload-file micro-service
     * @param file
     * @return JSON String
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadFile(@RequestPart("file") MultipartFile file);

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
