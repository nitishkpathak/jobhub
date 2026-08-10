package com.jobhub.jobhub.service;

import com.jobhub.jobhub.dto.UserRequestDto;
import com.jobhub.jobhub.dto.UserResponseDto;
import com.jobhub.jobhub.entity.User;
import com.jobhub.jobhub.exception.DuplicateEmailException;
import com.jobhub.jobhub.exception.ResourceNotFoundException;
import com.jobhub.jobhub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create User
    public UserResponseDto createUser(UserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateEmailException("User already exists with email: " + requestDto.getEmail());
        }

        User user = mapToEntity(requestDto);
        User savedUser = userRepository.save(user);
        return mapToResponseDto(savedUser);
    }

    // Get All Users
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Get User By ID
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponseDto(user);
    }

    // Update User
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check if updating to an email that belongs to another user
        if (!existingUser.getEmail().equalsIgnoreCase(requestDto.getEmail()) &&
                userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateEmailException("Email already in use: " + requestDto.getEmail());
        }

        existingUser.setName(requestDto.getName());
        existingUser.setEmail(requestDto.getEmail());
        if (requestDto.getPassword() != null && !requestDto.getPassword().isBlank()) {
            existingUser.setPassword(requestDto.getPassword());
        }
        existingUser.setRole(requestDto.getRole());
        existingUser.setPhone(requestDto.getPhone());
        existingUser.setLocation(requestDto.getLocation());
        existingUser.setBio(requestDto.getBio());
        existingUser.setSkills(requestDto.getSkills());
        existingUser.setExperience(requestDto.getExperience());

        User updatedUser = userRepository.save(existingUser);
        return mapToResponseDto(updatedUser);
    }

    // Delete User
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Helper: Map Entity -> UserResponseDto
    public UserResponseDto mapToResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhone(),
                user.getLocation(),
                user.getBio(),
                user.getSkills(),
                user.getExperience(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    // Helper: Map UserRequestDto -> Entity
    private User mapToEntity(UserRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setPhone(dto.getPhone());
        user.setLocation(dto.getLocation());
        user.setBio(dto.getBio());
        user.setSkills(dto.getSkills());
        user.setExperience(dto.getExperience());
        return user;
    }
}