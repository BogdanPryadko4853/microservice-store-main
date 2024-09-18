package com.bogdan.authservice.dto;

import com.bogdan.authservice.enums.Role;
import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String username;
    private String password;
    private Role role;
}
