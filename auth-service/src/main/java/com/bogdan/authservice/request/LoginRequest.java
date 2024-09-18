package com.bogdan.authservice.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;
}
