package com.java.spring_7_di;

import com.java.spring_7_di.controllers.MyController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Spring7DiApplication {

    public static void main(String[] args) {

        var ctx = SpringApplication.run(Spring7DiApplication.class, args);

        MyController myController = ctx.getBean(MyController.class);

        System.out.println(myController.sayHello());
        ;
    }

}
