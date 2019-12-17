package com.quart.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.quart.job.api")
public class QuartJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuartJobApplication.class, args);
    }
}
