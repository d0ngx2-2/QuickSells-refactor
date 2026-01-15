package com.example.quicksells.domain.user.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.user.model.request.UserUpdateRequest;
import com.example.quicksells.domain.user.model.response.UserGetResponse;
import com.example.quicksells.domain.user.model.response.UserUpdateResponse;
import com.example.quicksells.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 내 정보 조회 API
     *
     */
    @GetMapping("/users/me")
    public ResponseEntity<CommonResponse> getMyPage(@AuthenticationPrincipal AuthUser authUser) {

        UserGetResponse response = userService.getMyPage(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("내 정보 조회 성공", response));
    }

    /**
     * 전체 유저 정보 조회 API
     * hasRole (ADMIN)
     *
     */
    @GetMapping("/admin/users")
    public ResponseEntity<CommonResponse> getAllUsers(Pageable pageable) {

        Page<UserGetResponse> response = userService.getAllUsers(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("전체 회원 조회 성공", response));
    }



    /**
     * 내 정보 수정 API
     *
     * @param request 내 정보 수정 요청 정보
     */
    @PatchMapping("/users/me")
    public ResponseEntity<CommonResponse> update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody UserUpdateRequest request) {

        UserUpdateResponse response = userService.update(authUser, request);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("내 정보 수정 성공", response));

    }

    /**
     * 회원 탈퇴 API
     *
     */
    @DeleteMapping("/users/me")
    public ResponseEntity<CommonResponse> delete(@AuthenticationPrincipal AuthUser authUser) {

        userService.delete(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("회원 탈퇴 성공"));
    }
}
