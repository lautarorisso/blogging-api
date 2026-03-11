package com.lautarorisso.blogging_platform_api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {

    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request) {

    }
}
