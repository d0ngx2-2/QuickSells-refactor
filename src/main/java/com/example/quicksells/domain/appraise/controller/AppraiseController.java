package com.example.quicksells.domain.appraise.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.appraise.model.request.AppraiseAuctionProceedRequest;
import com.example.quicksells.domain.appraise.model.request.AppraiseUpdateRequest;
import com.example.quicksells.domain.appraise.model.response.*;
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

// swagger 컨트롤러 그룹화
@Tag(name = "사용자 감정(appraise) 관리")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppraiseController {

    private final AppraiseService appraiseService;

    /**
     * 상품별 감정 목록 전체 조회 (페이징)
     */
    @Operation(summary = "상품별 감정 목록 전체 조회")
    @GetMapping("/items/{itemId}/appraises")
    public ResponseEntity<PageResponse> getAppraises(@PathVariable Long itemId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "desc") String order, @AuthenticationPrincipal AuthUser authUser) {

        // 정렬 방향 결정 (기본: 최신순)
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Pageable 생성 (생성일자 기준 정렬)
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        Page<AppraiseGetAllResponse> response = appraiseService.getAppraisesByItemId(itemId, pageable, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(PageResponse.success("상품별 감정 전체 조회에 성공했습니다.", response));
    }

    /**
     * 상품별 감정 단건 조회
     */
    @Operation(summary = "상품별 감정 단건 조회")
    @GetMapping("/items/{itemId}/appraises/{id}")
    public ResponseEntity<CommonResponse> getAppraise(@PathVariable Long id, @PathVariable Long itemId,@AuthenticationPrincipal AuthUser authUser) {

        AppraiseGetResponse response = appraiseService.getAppraise(id, itemId, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품별 감정 단 건 조회에 성공했습니다.", response));
    }

    /**
     * 감정 선택 (판매자)
     *
     * 1. 판매자가 감정을 선택하여 즉시 판매 - 현재 감정 선택 API
     * 2. 감정사가 제시한 감정가가 마음에 들지 않는 경우 경매 처리 > 경매 생성 API에서 진행
     */
    @Operation(summary = "감정 선택(판매자)")
    @PutMapping("/appraises/{id}")
    public ResponseEntity<CommonResponse> updateAppraise(@PathVariable Long id, @Valid @RequestBody AppraiseUpdateRequest request, @AuthenticationPrincipal AuthUser authUser) {

        AppraiseUpdateResponse response = appraiseService.updateAppraise(id, request, authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("감정 선택에 성공했습니다.", response));
    }

    /**
     * 즉시 판매 확정
     */
    @Operation(summary = "즉시 판매")
    @PostMapping("/appraises/{id}/immediate-sell")
    public ResponseEntity<CommonResponse> confirmImmediateSell(@PathVariable Long id) {

        AppraiseImmediateSellResponse response = appraiseService.confirmImmediateSell(id);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("선택한 감정가로 즉시 판매합니다.", response));
    }

    /**
     * 경매 진행 확정 및 생성
     */
    @Operation(summary = "경매 진행")
    @PostMapping("/appraises/{id}/auction-proceed")
    public ResponseEntity<CommonResponse> confirmAuctionWithCreate(@PathVariable Long id, @Valid @RequestBody AppraiseAuctionProceedRequest request) {

        AppraiseAuctionProceedResponse response = appraiseService.confirmAuctionWithCreate(id, request.getTimeOption());

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("선택한 감정가로 경매 진행합니다.", response));
    }

}
