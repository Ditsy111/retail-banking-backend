package com.aurelia.banking.service;

import com.aurelia.banking.dto.LoginRequest;
import com.aurelia.banking.dto.RegisterRequest;
import com.aurelia.banking.entity.Role;
import com.aurelia.banking.entity.User;
import com.aurelia.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName((request.getLastName()))
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);
    }

    public String login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email address"));

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(), //this is the normal password from dto
                        user.getPassword()); /*this is the hashed password(as password stored in users means in
                                                database is stored in hashed format only so to fetch hash value we
                                                take it from the database*/


        if (!passwordMatches) {
            throw new RuntimeException("Password Invalid");
        }

        return jwtService.generateToken(
                user.getEmail()
        );
    }
}