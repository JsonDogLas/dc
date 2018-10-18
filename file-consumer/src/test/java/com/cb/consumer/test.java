package com.cb.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

/**
 * @author jdl
 * @date 2018/10/10 16:33
 */
public class test {

    @Autowired
    public static LoadBalancerClient balancerClient;

    public static void main(String[] args) {
        ServiceInstance instance = balancerClient.choose("file-service");
        System.out.println(instance.getScheme());
        System.out.println(instance.getHost());
        System.out.println(instance.getPort());
        System.out.println(instance.getUri());
        System.out.println(instance.getMetadata());
    }
}
