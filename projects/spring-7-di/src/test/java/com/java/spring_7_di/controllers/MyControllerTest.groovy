package com.java.spring_7_di.controllers

import org.junit.jupiter.api.Test

class MyControllerTest {
    @Test
    void testSayHello() {
        MyController controller = new MyController();

        println controller.sayHello()
    }
}
