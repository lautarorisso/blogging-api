package com.lautarorisso.blogging_platform_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lautarorisso.blogging_platform_api.dto.AuthResponse;
import com.lautarorisso.blogging_platform_api.dto.LoginRequest;
import com.lautarorisso.blogging_platform_api.dto.RegisterRequest;
import com.lautarorisso.blogging_platform_api.security.JwtService;
import com.lautarorisso.blogging_platform_api.service.AuthService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@PreAuthorize("permitAll()")
@RequiredArgsConstructor
@Valid
public class AuthController {
    public final AuthService authService;
    public final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

}
