package com.northstar.portfolio.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private UUID userId;

    private String firstName;

    private String lastName;

    private String email;

    private Set<String> roles;

    private String token;
}