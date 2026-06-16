package com.aurelia.banking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello(Authentication authentication) {

        return "Hello "
                + authentication.getName();
    }
}