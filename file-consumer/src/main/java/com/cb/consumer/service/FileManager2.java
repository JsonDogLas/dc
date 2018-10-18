package com.cb.consumer.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author jdl
 */
@Service
public class FileManager2 {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    public LoadBalancerClient balancerClient;

    public String getFileUri(String signId) {
        return restTemplate.getForObject("http://file-service/uri/" + signId,String.class);
    }

    public String uploadFileByString(Object map) {
        return restTemplate.postForObject("http://file-service/upload/string",map,String.class);
    }

    @HystrixCommand(fallbackMethod = "errorHandle")
    public String getServiceUri(String serviceId) {
        return balancerClient.choose(serviceId).getUri().toString();
    }

    public String errorHandle(String serviceId) {
        return serviceId + " not found";
    }
}
