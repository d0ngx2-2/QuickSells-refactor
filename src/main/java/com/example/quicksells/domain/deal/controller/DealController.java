package com.example.quicksells.domain.deal.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.deal.model.request.DealCreateRequest;
import com.example.quicksells.domain.deal.model.response.DealCreateResponse;
import com.example.quicksells.domain.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DealController {

    private final DealService dealService;

    /**
     * 거래 생성 API
     */
    @PostMapping("/deals")
    public ResponseEntity<CommonResponse> createDeal (@RequestBody DealCreateRequest request) {

        DealCreateResponse response = dealService.createDeal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("거래가 생성되었습니다.", response));
    }
}
