package com.laya.jobplacement.controller;

import com.laya.jobplacement.entity.LoginResponse;
import com.laya.jobplacement.entity.Student;
import com.laya.jobplacement.entity.User;
import com.laya.jobplacement.repository.StudentRepository;
import com.laya.jobplacement.repository.UserRepository;
import com.laya.jobplacement.security.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserController(
            UserRepository userRepository,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        // Check duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Email already registered");
        }

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Save user
        User savedUser = userRepository.save(user);

        // Create student profile only for STUDENT
        if ("STUDENT".equalsIgnoreCase(savedUser.getRole())) {

            Student student = new Student();

            student.setName(savedUser.getName());
            student.setEmail(savedUser.getEmail());

            studentRepository.save(student);
        }

        // Generate JWT
        String token = jwtService.generateToken(
                savedUser.getEmail(),
                savedUser.getRole()
        );

        LoginResponse response = new LoginResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                token
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        // Find user by email
        User existingUser = userRepository
                .findByEmail(user.getEmail())
                .orElse(null);

        // User not found
        if (existingUser == null) {
            return ResponseEntity
                    .status(401)
                    .body("Invalid email or password");
        }

        // Check password
        boolean passwordMatches = passwordEncoder.matches(
                user.getPassword(),
                existingUser.getPassword()
        );

        if (!passwordMatches) {
            return ResponseEntity
                    .status(401)
                    .body("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtService.generateToken(
                existingUser.getEmail(),
                existingUser.getRole()
        );

        // Create login response
        LoginResponse response = new LoginResponse(
                existingUser.getId(),
                existingUser.getName(),
                existingUser.getEmail(),
                existingUser.getRole(),
                token
        );

        return ResponseEntity.ok(response);
    }
}