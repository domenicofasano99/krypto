package com.bok.krypto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("/hello")
    public String hello(@RequestParam("userId") Long userId) {
        return userId + "hello from the other side";
    }
}
