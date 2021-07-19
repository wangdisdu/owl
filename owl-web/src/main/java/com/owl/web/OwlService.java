package com.owl.web;

import com.owl.web.common.exception.EnableExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableExceptionHandler
public class OwlService {
    public static void main(String[] args) {
        OwlApplication.lockPid();
        SpringApplication.run(OwlService.class, args);
    }
}
