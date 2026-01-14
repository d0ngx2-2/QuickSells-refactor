package com.example.quicksells.domain.auth.model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthLoginResponse {

    private final String token;

    public static AuthLoginResponse from(String token) {
        return new AuthLoginResponse(token);
    }
}
