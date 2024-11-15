package com.sample.account_service.controller;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/authInfo")
    public String login(AbstractAuthenticationToken token) {
        System.out.println(token);
        return token.toString();
    }

}
