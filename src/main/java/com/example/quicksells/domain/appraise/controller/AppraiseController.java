package com.example.quicksells.domain.appraise.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.appraise.model.request.AppraiseCreateRequest;
import com.example.quicksells.domain.appraise.model.request.AppraiseUpdateRequest;
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

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("감정 생성에 성공했습니다.", response));
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
    @GetMapping("/appraises/{id}/items/{itemId}")
    public ResponseEntity<CommonResponse> getAppraise(@PathVariable Long id, @PathVariable Long itemId,@AuthenticationPrincipal AuthUser authUser) {

        AppraiseResponse response = appraiseService.getAppraise(id, itemId, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품별 감정 단 건 조회에 성공했습니다.", response));
    }

    /**
     * 감정 선택 (판매자)
     *
     * 1. 판매자가 감정을 선택하여 즉시 판매 - 현재 감정 선택 API
     * 2. 감정사가 제시한 감정가가 마음에 들지 않는 경우 경매 처리 > 경매 생성 API에서 진행
     */
    @PutMapping("/appraises/{id}")
    public ResponseEntity<CommonResponse> updateAppraise(@PathVariable Long id, @Valid @RequestBody AppraiseUpdateRequest request, @AuthenticationPrincipal AuthUser authUser) {

        AppraiseResponse response = appraiseService.updateAppraise(id, request, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("감정 선택에 성공했습니다.", response));
    }

    /**
     * 감정 삭제 (ADMIN 권한)
     *
     * 감정사가 자신이 작성한 감정 제안을 삭제
     * - 본인이 작성한 감정만 삭제 가능
     * - 선택된 감정(isSelected = true)은 삭제 불가
     */
    @DeleteMapping("/admin/appraises/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse> deleteAppraise(@PathVariable Long itemId, @AuthenticationPrincipal AuthUser authUser) {

        appraiseService.deleteAppraise(itemId, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("감정 삭제에 성공했습니다."));
    }
}
