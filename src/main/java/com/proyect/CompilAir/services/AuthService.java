package com.proyect.CompilAir.services;

import com.proyect.CompilAir.models.User;
import com.proyect.CompilAir.dto.request.LoginRequest;
import com.proyect.CompilAir.dto.request.RegisterRequest;
import com.proyect.CompilAir.dto.response.AuthResponse;
import com.proyect.CompilAir.repositories.IUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final IUserRepository iUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtService jwtService, IUserRepository iUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.iUserRepository = iUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }
    public AuthResponse login(LoginRequest login) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

        UserDetails users = (UserDetails) iUserRepository.findByUsername(login.getUsername()).orElseThrow();

        String token = jwtService.getTokenService(users);

        return new AuthResponse.Builder().token(token).role(((User)users).getRole()).build();
    }

    public AuthResponse register(RegisterRequest register) {
        User user =
                new User.Builder()
                        .username(register.getUsername())
                        .email(register.getEmail())
                        .password(passwordEncoder.encode(register.getPassword()))
                        .role(register.getRole())
                        .build();

        iUserRepository.save(user);

        return new AuthResponse.Builder()
                .token(jwtService.getTokenService((UserDetails) user))
                .role(register.getRole())
                .build();
    }
}
