package com.backend.springrestmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringRestMvcApplication {
    static void main(String[] args) {
        SpringApplication.run(SpringRestMvcApplication.class, args);
    }
}
