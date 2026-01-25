package com.example.quicksells.domain.appraise.controller;

import com.example.quicksells.common.enums.AppraiseStatus;
import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.appraise.model.request.AppraiseAdminUpdateRequest;
import com.example.quicksells.domain.appraise.model.request.AppraiseCreateRequest;
import com.example.quicksells.domain.appraise.model.response.AppraiseAdminGetAllResponse;
import com.example.quicksells.domain.appraise.model.response.AppraiseAdminGetResponse;
import com.example.quicksells.domain.appraise.model.response.AppraiseAdminUpdateResponse;
import com.example.quicksells.domain.appraise.model.response.AppraiseCreateResponse;
import com.example.quicksells.domain.appraise.service.AppraiseService;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자(감정사) 감정(appraise) 관리")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppraiseAdminController {

    private final AppraiseService appraiseService;

    /**
     * 감정 생성 (ADMIN(감정사) 권한)
     */
    @Operation(summary = "감정 생성(관리자)") // 각 API의 설명
    @PostMapping("/admin/items/{itemId}/appraises")
    public ResponseEntity<CommonResponse> createAdminAppraise(@PathVariable Long itemId, @Valid @RequestBody AppraiseCreateRequest request, @AuthenticationPrincipal AuthUser authUser) {

        AppraiseCreateResponse response = appraiseService.createAppraise(itemId, request, authUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("감정 생성에 성공했습니다.", response));
    }

    /**
     * 관리자 본인이 감정한 상품 목록 조회 (페이징)
     */
    @Operation(summary = "감정 전체 조회(관리자)")
    @GetMapping("/admin/appraises")
    public ResponseEntity<PageResponse> getMyAdminAppraises(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) AppraiseStatus status, @AuthenticationPrincipal AuthUser authUser) {

        // Pageable 생성 (생성일자 기준 정렬)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<AppraiseAdminGetAllResponse> responses = appraiseService.getMyAdminAppraises(authUser.getId(), status, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(PageResponse.success("감정 전체 조회에 성공했습니다.", responses));
    }

    /**
     * 관리자 본인이 감정한 상품 상세 조회
     */
    @Operation(summary = "감정 단 건 조회(관리자)")
    @GetMapping("/admin/appraises/{id}")
    public ResponseEntity<CommonResponse> getMyAdminAppraiseDetail(@PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {

        AppraiseAdminGetResponse response = appraiseService.getMyAdminAppraiseDetail(id, authUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("감정 단 건 조회에 성공했습니다.", response));
    }

    /**
     * 관리자 본인이 감정한 감정가 수정
     */
    @Operation(summary = "감정 수정(관리자)")
    @PatchMapping("/admin/appraises/{id}")
    public ResponseEntity<CommonResponse> updateMyAdminAppraise(@PathVariable Long id, @RequestBody @Valid AppraiseAdminUpdateRequest request, @AuthenticationPrincipal AuthUser authUser) {

        AppraiseAdminUpdateResponse response = appraiseService.updateMyAdminAppraise(id, request, authUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("감정가 수정에 성공했습니다.", response));
    }

    /**
     * 감정 삭제 (ADMIN 권한)
     *
     * 감정사가 자신이 작성한 감정 제안을 삭제
     * - 본인이 작성한 감정만 삭제 가능
     * - 선택된 감정(isSelected = true)은 삭제 불가
     */
    @Operation(summary = "감정 삭제(관리자)")
    @DeleteMapping("/admin/items/{itemId}/appraises")
    public ResponseEntity<CommonResponse> deleteAdminAppraise(@PathVariable Long itemId, @AuthenticationPrincipal AuthUser authUser) {

        appraiseService.deleteAppraise(itemId, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("감정 삭제에 성공했습니다."));
    }
}
