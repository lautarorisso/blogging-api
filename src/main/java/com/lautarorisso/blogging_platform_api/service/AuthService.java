package com.lautarorisso.blogging_platform_api.service;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lautarorisso.blogging_platform_api.dto.AuthResponse;
import com.lautarorisso.blogging_platform_api.dto.LoginRequest;
import com.lautarorisso.blogging_platform_api.dto.RegisterRequest;
import com.lautarorisso.blogging_platform_api.exception.InvalidCredentialsException;
import com.lautarorisso.blogging_platform_api.exception.UsernameAlreadyExistsException;
import com.lautarorisso.blogging_platform_api.model.UserEntity;
import com.lautarorisso.blogging_platform_api.repository.UserRepository;
import com.lautarorisso.blogging_platform_api.security.JwtService;
import com.lautarorisso.blogging_platform_api.security.Role;
import com.lautarorisso.blogging_platform_api.security.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthResponse register(RegisterRequest request) {
        String username = request.username();
        String password = request.password();
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        userRepository.save(user);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(),
                user.getPassword(), authorities);
        String accessToken = jwtService.createToken(auth);

        return new AuthResponse(user.getUsername(), "User registered successfully", accessToken, true);
    }

    public AuthResponse login(LoginRequest request) {
        String username = request.username();
        String password = request.password();
        Authentication auth = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String accessToken = jwtService.createToken(auth);
        return new AuthResponse(username, "Login successful", accessToken, true);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(
                username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
