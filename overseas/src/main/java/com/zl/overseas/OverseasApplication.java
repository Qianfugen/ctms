package com.zl.overseas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author root
 */
@SpringBootApplication
@EnableEurekaClient
public class OverseasApplication {

    public static void main(String[] args) {
        SpringApplication.run(OverseasApplication.class, args);
    }

}
