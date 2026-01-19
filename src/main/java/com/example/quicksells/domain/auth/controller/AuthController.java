package com.example.quicksells.domain.auth.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.auth.model.request.AuthLoginRequest;
import com.example.quicksells.domain.auth.model.request.AuthSignupRequest;
import com.example.quicksells.domain.auth.model.response.AuthLoginResponse;
import com.example.quicksells.domain.auth.model.response.AuthSignupResponse;
import com.example.quicksells.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원가입/로그인(Auth) 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 API
     *
     * @param request 회원가입 요청 정보
     */
    @Operation(summary = "회원가입")
    @PostMapping("/auth/signup")
    public ResponseEntity<CommonResponse> createUser(@Valid @RequestBody AuthSignupRequest request) {

        AuthSignupResponse response = authService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("회원 가입 성공.", response));
    }

    /**
     * 로그인 API
     *
     * @param request 로그인 요청 정보
     */
    @Operation(summary = "로그인")
    @PostMapping("/auth/login")
    public ResponseEntity<CommonResponse> login(@Valid @RequestBody AuthLoginRequest request) {

        AuthLoginResponse response = authService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("로그인 성공", response));
    }
}
