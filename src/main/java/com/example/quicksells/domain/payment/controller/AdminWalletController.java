package com.example.quicksells.domain.payment.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.payment.model.request.AdminPointGrantRequest;
import com.example.quicksells.domain.payment.model.response.AdminPointGrantResponse;
import com.example.quicksells.domain.payment.model.response.PointTransactionGetResponse;
import com.example.quicksells.domain.payment.model.response.WalletGetResponse;
import com.example.quicksells.domain.payment.service.PointWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 포인트 지갑(Admin Wallet) 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/wallets")
public class AdminWalletController {

    private final PointWalletService pointWalletService;

    @Operation(summary = "관리자 : 포인트 지급")
    @PostMapping("/{userId}/grant")
    public ResponseEntity<?> grantPoint(
            @PathVariable Long userId,
            @Valid @RequestBody AdminPointGrantRequest request
    ) {
        AdminPointGrantResponse response =
                pointWalletService.grantPointResponse(userId, request.getAmount());

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("관리자 포인트 지급에 성공하였습니다.", response));
    }

    @Operation(summary = "관리자 : 특정 유저 지갑 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserWallet(@PathVariable Long userId) {
        WalletGetResponse response = pointWalletService.getUserWalletResponse(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("유저 지갑 조회를 성공하였습니다.", response));
    }

    @Operation(summary = "관리자 : 특정 유저 거래내역 조회")
    @GetMapping("/{userId}/transactions")
    public ResponseEntity<?> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<PointTransactionGetResponse> result =
                pointWalletService.getUserTransactionsResponse(userId, page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(PageResponse.success("유저 거래내역 조회를 성공하였습니다.", result));
    }
}