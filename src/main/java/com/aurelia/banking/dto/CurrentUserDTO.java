package com.aurelia.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentUserDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String role;
}
