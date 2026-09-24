package com.java.spring_7_di.services;

public class GreetingServiceImpl implements GreetingService {
    @Override
    public String sayGreeting() {
        return "Hello from GreetingServiceImpl";
    }
}
