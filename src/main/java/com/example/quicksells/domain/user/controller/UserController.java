package com.example.quicksells.domain.user.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.user.model.response.UserGetResponse;
import com.example.quicksells.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * 내 정보 조회 API
     *
     */
    @GetMapping("/me")
    public ResponseEntity<CommonResponse> getMyPage(@AuthenticationPrincipal AuthUser authUser) {

        UserGetResponse response = userService.getMyPage(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("내 정보 조회 성공", response));
    }
}
