package com.bogdan.authservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String token;
}
