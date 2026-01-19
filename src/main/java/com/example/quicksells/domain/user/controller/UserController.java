package com.example.quicksells.domain.user.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.user.model.request.UserRoleUpdateRequest;
import com.example.quicksells.domain.user.model.request.UserUpdateRequest;
import com.example.quicksells.domain.user.model.response.UserGetResponse;
import com.example.quicksells.domain.user.model.response.UserUpdateResponse;
import com.example.quicksells.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자(user) 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 내 정보 조회 API
     *
     */
    @Operation(summary = "내 정보 조회")
    @GetMapping("/users/me")
    public ResponseEntity<CommonResponse> getMyPage(@AuthenticationPrincipal AuthUser authUser) {

        UserGetResponse response = userService.getMyPage(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("내 정보 조회 성공하셨습니다.", response));
    }



    /**
     * 내 정보 수정 API
     *
     * @param request 내 정보 수정 요청 정보
     */
    @Operation(summary = "내 정보 수정")
    @PatchMapping("/users/me")
    public ResponseEntity<CommonResponse> update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody UserUpdateRequest request) {

        UserUpdateResponse response = userService.update(authUser, request);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("내 정보 수정 성공하셨습니다.", response));

    }

    /**
     * 회원 탈퇴 API
     *
     */
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/users/me")
    public ResponseEntity<CommonResponse> delete(@AuthenticationPrincipal AuthUser authUser) {

        userService.delete(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("회원 탈퇴 성공하셨습니다."));
    }

    /**
     * 전체 유저 정보 조회 API
     * hasRole (ADMIN)
     *
     */
    @Operation(summary = "전체 사용자 정보 조회(관리자)")
    @GetMapping("/admin/users")
    public ResponseEntity<CommonResponse> getAllUsers(Pageable pageable) {

        Page<UserGetResponse> response = userService.getAllUsers(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("전체 회원 조회 성공하셨습니다.", response));
    }

    /**
     * 유저 권한 변경 API
     * hasRole (ADMIN)
     *
     */
    @Operation(summary = "사용자 권한 변경(관리자)")
    @PatchMapping("/admin/users/{userId}")
    public ResponseEntity<CommonResponse> updateRole(@PathVariable Long userId, @Valid @RequestBody UserRoleUpdateRequest request) {

        UserUpdateResponse response = userService.updateRole(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("유저 권한 변경 성공하셨습니다.", response));
    }
}
