package com.example.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String userid;
    private String passwordHash;
}
