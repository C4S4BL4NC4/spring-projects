package com.backend.sdjpadatarest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SdjpaDataRestApplication {

    // Spring data rest basically removes the hassle of implementing boilerplate by hand.

    static void main(String[] args) {

        SpringApplication.run(SdjpaDataRestApplication.class, args);
    }

}
