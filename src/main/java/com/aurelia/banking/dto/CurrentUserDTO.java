package com.aurelia.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentUserDTO {

    private String email;

    private String role;

}
