package com.example.quicksells.domain.payment.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.common.model.PageResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.payment.model.request.WithdrawRequest;
import com.example.quicksells.domain.payment.model.response.PointTransactionGetResponse;
import com.example.quicksells.domain.payment.model.response.WalletGetResponse;
import com.example.quicksells.domain.payment.model.response.WithdrawResponse;
import com.example.quicksells.domain.payment.service.PointWalletService;
import com.example.quicksells.domain.payment.service.WithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 포인트 지갑(Wallet) 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
public class WalletController {

    private final WithdrawalService withdrawalService;
    private final PointWalletService pointWalletService;

    @Operation(summary = "포인트 출금 : 내부 포인트 차감 + 거래내역(PointTransaction) 기록")
    @PostMapping("/withdrawals")
    public ResponseEntity<?> withdraw(
            @Valid @RequestBody WithdrawRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        WithdrawResponse response = withdrawalService.withdraw(authUser, request.getAmount());

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("출금이 완료되었습니다.", response));
    }

    @Operation(summary = "내 지갑 조회")
    @GetMapping("/me")
    public ResponseEntity<?> getMyWallet(@AuthenticationPrincipal AuthUser authUser) {
        WalletGetResponse response = pointWalletService.getMyWalletResponse(authUser.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success("내 지갑 조회를 성공하였습니다.", response));
    }

    @Operation(summary = "내 포인트 거래내역 조회")
    @GetMapping("/transactions")
    public ResponseEntity<?> getMyTransactions(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<PointTransactionGetResponse> result =
                pointWalletService.getMyTransactionsResponse(authUser.getId(), page, size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(PageResponse.success("내 포인트 거래내역 조회를 성공하였습니다.", result));
    }
}