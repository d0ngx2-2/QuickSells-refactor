package com.example.quicksells.domain.auction.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.auction.model.request.AuctionCreateRequest;
import com.example.quicksells.domain.auction.model.request.AuctionUpdateRequest;
import com.example.quicksells.domain.auction.model.response.AuctionCreateResponse;
import com.example.quicksells.domain.auction.model.response.AuctionGetAllResponse;
import com.example.quicksells.domain.auction.model.response.AuctionGetResponse;
import com.example.quicksells.domain.auction.model.response.AuctionUpdateResponse;
import com.example.quicksells.domain.auction.service.AuctionService;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/auctions/{id}")
    public ResponseEntity<CommonResponse> getAuction(@PathVariable Long id) {

        AuctionGetResponse result = auctionService.getAuction(id);

        return ResponseEntity.ok(CommonResponse.success("경매 상세 조회 성공", result));
    }

    @PutMapping("/auctions/{id}")
    public ResponseEntity<CommonResponse> updateBidPrice(@PathVariable Long id, @Valid @RequestBody AuctionUpdateRequest request, @AuthenticationPrincipal AuthUser authUser) {

        AuctionUpdateResponse result = auctionService.updateBidPrice(id, request, authUser);

        return ResponseEntity.ok(CommonResponse.success("상품 입찰 성공", result));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/auctions/{id}")
    public ResponseEntity<CommonResponse> deleteAuction(@PathVariable Long id) {

        auctionService.deleteAuction(id);

        return ResponseEntity.ok(CommonResponse.success("경매 삭제 성공"));
    }
}
