package com.lautarorisso.blogging_platform_api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lautarorisso.blogging_platform_api.dto.AuthResponse;
import com.lautarorisso.blogging_platform_api.dto.LoginRequest;
import com.lautarorisso.blogging_platform_api.dto.RegisterRequest;
import com.lautarorisso.blogging_platform_api.exception.InvalidCredentialsException;
import com.lautarorisso.blogging_platform_api.exception.UserNotFoundException;
import com.lautarorisso.blogging_platform_api.exception.UsernameAlreadyExistsException;
import com.lautarorisso.blogging_platform_api.model.UserEntity;
import com.lautarorisso.blogging_platform_api.repository.UserRepository;
import com.lautarorisso.blogging_platform_api.security.JwtService;
import com.lautarorisso.blogging_platform_api.security.Role;
import com.lautarorisso.blogging_platform_api.security.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private UserEntity userEntity;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("testuser", "password123");
        loginRequest = new LoginRequest("testuser", "password123");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testuser");
        userEntity.setPassword("encodedPassword");
        userEntity.setRole(Role.USER);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Role.USER.name()));
        userDetails = new User("testuser", "encodedPassword", authorities);
    }

    @Test
    @SuppressWarnings("null")
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(jwtService.createToken(any(Authentication.class))).thenReturn("jwt-token-123");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("testuser", response.username());
        assertEquals("User registered successfully", response.message());
        assertEquals("jwt-token-123", response.jwt());
        assertTrue(response.status());

        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(jwtService, times(1)).createToken(any(Authentication.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            authService.register(registerRequest);
        });

        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(passwordEncoder, never()).encode(anyString());
        verify(jwtService, never()).createToken(any(Authentication.class));
    }

    @Test
    void shouldLoginUserSuccessfully() {
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.createToken(any(Authentication.class))).thenReturn("jwt-token-123");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("testuser", response.username());
        assertEquals("Login successful", response.message());
        assertEquals("jwt-token-123", response.jwt());
        assertTrue(response.status());

        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
        verify(jwtService, times(1)).createToken(any(Authentication.class));
    }

    @Test
    void shouldThrowExceptionOnInvalidPassword() {

        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login(new LoginRequest("testuser", "wrongpassword"));
        });

        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("wrongpassword", "encodedPassword");
        verify(jwtService, never()).createToken(any(Authentication.class));
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {

        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        Authentication auth = authService.authenticate("testuser", "password123");

        assertNotNull(auth);
        assertEquals("testuser", auth.getPrincipal());
        assertTrue(auth.isAuthenticated());
        assertEquals(1, auth.getAuthorities().size());

        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
    }

    @Test
    void shouldThrowExceptionOnInvalidCredentials() {

        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> {
            authService.authenticate("testuser", "wrongpassword");
        });

        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("wrongpassword", "encodedPassword");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userDetailsService.loadUserByUsername("nonexistent")).thenThrow(
                new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> {
            authService.authenticate("nonexistent", "password123");
        });

        verify(userDetailsService, times(1)).loadUserByUsername("nonexistent");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}
