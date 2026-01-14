package com.example.quicksells.domain.auth.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.auth.model.request.UserCreateRequest;
import com.example.quicksells.domain.auth.model.response.UserCreateResponse;
import com.example.quicksells.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 API
     * @param request 회원가입 요청 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> createUser(@Valid @RequestBody UserCreateRequest request) {

        UserCreateResponse response = authService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("회원 가입 성공.", response));
    }
}
