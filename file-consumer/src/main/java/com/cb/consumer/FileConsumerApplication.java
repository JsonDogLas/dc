package com.cb.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author jdl
 */

@SpringBootApplication
@EnableHystrix
@EnableFeignClients
@EnableSwagger2
public class FileConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileConsumerApplication.class, args);
	}
}
