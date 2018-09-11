package com.cb.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author jdl
 */
@Service
public class FileManager2 {
    @Autowired
    RestTemplate restTemplate;

    public String uploadFileByString(Object map) {
        return restTemplate.postForObject("http://file-service/upload/string",map,String.class);
    }
}
