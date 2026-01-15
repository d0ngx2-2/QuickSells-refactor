package com.example.quicksells.domain.deal.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.deal.model.request.DealCreateRequest;
import com.example.quicksells.domain.deal.model.response.DealCreateResponse;
import com.example.quicksells.domain.deal.model.response.DealGetResponse;
import com.example.quicksells.domain.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * 거래 내역 전체 조회
     */
    @GetMapping("/deals/{id}")
    public ResponseEntity<CommonResponse> getDealDetail(@PathVariable Long id) {

        DealGetResponse response = dealService.getDealDetail(id);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("거래 상세 조회를 성공했습니다", response));
    }
}
