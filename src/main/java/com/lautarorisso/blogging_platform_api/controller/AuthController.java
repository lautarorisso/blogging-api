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

import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.lautarorisso.blogging_platform_api.dto.ErrorResponse;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Authentication", description = "Authentication management APIs")
@RestController
@RequestMapping("/auth")
@PreAuthorize("permitAll()")
@RequiredArgsConstructor
public class AuthController {
    public final AuthService authService;
    public final JwtService jwtService;

    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided registration details", security = {}, tags = {
            "Authentication" })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration details", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterRequest.class)))
    @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid registration data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate user", description = "Authenticates a user with email and password, returning a JWT token", security = {}, tags = {
            "Authentication" })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User login credentials", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class)))
    @ApiResponse(responseCode = "200", description = "User authenticated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid login credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

}
