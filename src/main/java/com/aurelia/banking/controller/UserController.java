package com.aurelia.banking.controller;

import com.aurelia.banking.dto.CurrentUserDTO;
import com.aurelia.banking.dto.UpdateProfileRequest;
import com.aurelia.banking.entity.User;
import com.aurelia.banking.repository.UserRepository;
import com.aurelia.banking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public CurrentUserDTO getCurrentUser(
            Authentication authentication
    ) {
        return userService.getCurrentUser(
                authentication.getName()
        );
    }

    @PutMapping("/profile")
    public CurrentUserDTO updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return userService.updateProfile(
                authentication.getName(),   ////this is parameter for the function updateProfile in UserService
                request
        ); /////// and this whole return type is currentDTO type

    }

}
