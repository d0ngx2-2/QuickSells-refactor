package com.example.quicksells.domain.deal.controller;

import com.example.quicksells.common.enums.DealType;
import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.deal.model.request.DealCreateRequest;
import com.example.quicksells.domain.deal.model.response.DealCreateResponse;
import com.example.quicksells.domain.deal.model.response.DealGetResponse;
import com.example.quicksells.domain.deal.model.response.DealListResponse;
import com.example.quicksells.domain.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DealController {

    private final DealService dealService;

    /**
     * 거래 생성 API
     */
    @PostMapping("/deals")
    public ResponseEntity<CommonResponse> createDeal(@RequestBody DealCreateRequest request) {

        DealCreateResponse response = dealService.createDeal(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("거래가 생성되었습니다.", response));
    }

    /**
     * 거래 내역 상세 조회
     */
    @GetMapping("/deals/{id}")
    public ResponseEntity<CommonResponse> getDealDetail(@PathVariable Long id) {

        DealGetResponse response = dealService.getDealDetail(id);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("거래 상세 조회를 성공했습니다", response));
    }

    /**
     * 거래 조회 (구매 / 판매)
     */
    @GetMapping("/deals")
    public ResponseEntity<CommonResponse> getDeals(@RequestParam DealType type, @AuthenticationPrincipal AuthUser user) {

        List<DealListResponse> response = dealService.getDeals(type, user);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("거래 조회를 성공했습니다.", response));
    }
}
