package com.java.spring_7_di.controllers;

import com.java.spring_7_di.services.GreetingService;
import com.java.spring_7_di.services.GreetingServiceImpl;
import org.springframework.stereotype.Controller;

@Controller
public class MyController {

    private final GreetingService greetingService;

    public MyController() {
        this.greetingService = new GreetingServiceImpl();
    }

    public String sayHello() {
        return greetingService.sayGreeting();
    }
}
