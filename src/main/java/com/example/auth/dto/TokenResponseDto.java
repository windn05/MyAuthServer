package com.example.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponseDto {
    private final String token;
    private String userName;
    private String message;

}
