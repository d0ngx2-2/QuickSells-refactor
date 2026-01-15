package com.example.quicksells.domain.appraise.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.appraise.model.request.AppraiseCreateRequest;
import com.example.quicksells.domain.appraise.model.response.AppraiseResponse;
import com.example.quicksells.domain.appraise.service.AppraiseSevice;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppraiseController {

    private final AppraiseSevice appraiseService;

    /**
     * 감정 생성 (ADMIN(감정사) 권한)
     */
    @PostMapping("/admin/appraises/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse> createAppraise(@PathVariable Long itemId, @Valid @RequestBody AppraiseCreateRequest request, @AuthenticationPrincipal AuthUser authUser) {

        Long adminId = authUser.getId();

        AppraiseResponse response = appraiseService.createAppraise(itemId, request, adminId);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("감정 조회에 성공했습니다.", response));
    }

    /**
     * 상품별 감정 목록 전체 조회 (페이징)
     */
    @GetMapping("/appraises/items/{itemId}")
    public ResponseEntity<PageResponse> getAppraises(@PathVariable Long itemId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "desc") String order, @AuthenticationPrincipal AuthUser authUser) {

        // 정렬 방향 결정 (기본: 최신순)
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Pageable 생성 (생성일자 기준 정렬)
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        Page<AppraiseResponse> response = appraiseService.getAppraisesByItemId(itemId, pageable, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(PageResponse.success("상품별 감정 전체 조회에 성공했습니다.", response));
    }

    /**
     * 상품별 감정 단건 조회
     */
    @GetMapping("/appraises/{appraiseId}/items/{itemId}")
    public ResponseEntity<CommonResponse> getAppraise(@PathVariable Long itemId, @PathVariable Long appraiseId, @AuthenticationPrincipal AuthUser authUser) {

        AppraiseResponse response = appraiseService.getAppraise(itemId, appraiseId ,authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품별 감정 단 건 조회에 성공했습니다.", response));
    }
}
