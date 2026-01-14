package com.example.quicksells.domain.auction.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.auction.dto.request.AuctionCreateRequest;
import com.example.quicksells.domain.auction.dto.response.AuctionCreateResponse;
import com.example.quicksells.domain.auction.dto.response.AuctionGetAllResponse;
import com.example.quicksells.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/auctions")
    public ResponseEntity<CommonResponse> createAuction(@RequestBody AuctionCreateRequest request) {

        AuctionCreateResponse result = auctionService.saveAuction(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("경매장 등록 성공", result));
    }

    @GetMapping("/auctions")
    public ResponseEntity<PageResponse> getAllAuction(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<AuctionGetAllResponse> page = auctionService.getAllAuction(pageable);

        return ResponseEntity.ok(PageResponse.success("경매 목록 조회 성공", page));
    }
}
