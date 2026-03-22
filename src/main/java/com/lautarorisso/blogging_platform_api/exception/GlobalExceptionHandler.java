package com.lautarorisso.blogging_platform_api.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lautarorisso.blogging_platform_api.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                log.warn("Resource not found: {}", ex.getMessage());
                ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
                                "RESOURCE_NOT_FOUND",
                                LocalDateTime.now(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
                log.warn("User not found: {}", ex.getMessage());
                ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "USER_NOT_FOUND",
                                LocalDateTime.now(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(ForbiddenException.class)
        public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
                log.warn("Access denied: {}", ex.getMessage());
                ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), "FORBIDDEN",
                                LocalDateTime.now(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentials(InvalidCredentialsException ex,
                        HttpServletRequest request) {
                log.warn("Invalid credentials: {}", ex.getMessage());
                ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(),
                                "INVALID_CREDENTIALS",
                                LocalDateTime.now(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        @ExceptionHandler(JwtVerificationException.class)
        public ResponseEntity<ErrorResponse> handleJWTVerification(JwtVerificationException ex,
                        HttpServletRequest request) {
                log.warn("Invalid token");
                ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(),
                                "JWT_VERIFICATION_ERROR",
                                LocalDateTime.now(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        @ExceptionHandler(UsernameAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex,
                        HttpServletRequest request) {
                log.warn("Username already exists: {}", ex.getMessage());
                ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(),
                                "USERNAME_ALREADY_EXISTS",
                                LocalDateTime.now(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
                log.error("An unexpected error occurred: {}", ex);
                ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "An unexpected error occurred", "INTERNAL_SERVER_ERROR", LocalDateTime.now(),
                                request.getRequestURI());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

}
