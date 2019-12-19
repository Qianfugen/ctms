package com.zl.domestic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author root
 */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan("com.zl")
@MapperScan("com.zl.dao")
public class DomesticApplication {

    public static void main(String[] args) {
        SpringApplication.run(DomesticApplication.class, args);
    }

}
