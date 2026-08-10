package com.jobhub.jobhub.service;

import com.jobhub.jobhub.dto.AuthResponse;
import com.jobhub.jobhub.dto.LoginRequest;
import com.jobhub.jobhub.dto.RegisterRequest;
import com.jobhub.jobhub.dto.UserResponseDto;
import com.jobhub.jobhub.entity.User;
import com.jobhub.jobhub.exception.DuplicateEmailException;
import com.jobhub.jobhub.exception.InvalidCredentialsException;
import com.jobhub.jobhub.repository.UserRepository;
import com.jobhub.jobhub.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // Register Candidate or Recruiter and generate JWT token
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email is already registered: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());
        user.setPhone(request.getPhone());
        user.setLocation(request.getLocation());
        user.setBio(request.getBio());
        user.setSkills(request.getSkills());
        user.setExperience(request.getExperience());

        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);
        UserResponseDto userResponseDto = userService.mapToResponseDto(savedUser);

        return new AuthResponse(jwtToken, userResponseDto);
    }

    // Authenticate Candidate or Recruiter and generate JWT token
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String jwtToken = jwtService.generateToken(user);
        UserResponseDto userResponseDto = userService.mapToResponseDto(user);

        return new AuthResponse(jwtToken, userResponseDto);
    }
}
