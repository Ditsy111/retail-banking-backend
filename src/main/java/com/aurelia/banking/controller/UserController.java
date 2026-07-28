package com.aurelia.banking.controller;

import com.aurelia.banking.dto.CurrentUserDTO;
import com.aurelia.banking.entity.User;
import com.aurelia.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    @GetMapping("/me")
    public CurrentUserDTO  getCurrentUser(Authentication authentication) {

        User user=userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        return new CurrentUserDTO(
                user.getEmail(),
                user.getRole().name()
        );
    }

}
