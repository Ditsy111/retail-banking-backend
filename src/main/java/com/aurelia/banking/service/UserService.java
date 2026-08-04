package com.aurelia.banking.service;

import com.aurelia.banking.dto.CurrentUserDTO;
import com.aurelia.banking.dto.UpdateProfileRequest;
import com.aurelia.banking.entity.User;
import com.aurelia.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Get logged-in user's profile
    public CurrentUserDTO getCurrentUser(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        return new CurrentUserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name()
        );
    }

    // Update profile
    public CurrentUserDTO updateProfile(
            String email,
            UpdateProfileRequest request
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());

        userRepository.save(user);

        return new CurrentUserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name()
        );
    }

}