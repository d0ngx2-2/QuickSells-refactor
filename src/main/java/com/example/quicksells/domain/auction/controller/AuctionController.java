package com.example.quicksells.domain.auction.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.auction.dto.request.AuctionCreateRequest;
import com.example.quicksells.domain.auction.dto.response.AuctionCreateResponse;
import com.example.quicksells.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<CommonResponse> createAuction(@RequestBody AuctionCreateRequest request) {
        AuctionCreateResponse result = auctionService.saveAuction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("경매장 등록 성공", result));
    }
}
