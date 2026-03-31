package com.example.quicksells.domain.payment.controller;

import com.example.quicksells.common.model.CommonResponse;
import com.example.quicksells.domain.auth.model.dto.AuthUser;
import com.example.quicksells.domain.payment.model.request.PaymentConfirmRequest;
import com.example.quicksells.domain.payment.model.request.PaymentOrderCreateRequest;
import com.example.quicksells.domain.payment.model.response.PaymentConfigResponse;
import com.example.quicksells.domain.payment.model.response.PaymentConfirmResponse;
import com.example.quicksells.domain.payment.model.response.PaymentOrderCreateResponse;
import com.example.quicksells.domain.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 결제(Payment) 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${toss.payments.client-key}")
    private String tossClientKey;

    @Operation(summary = "토스페이 주문 생성")
    @PostMapping("/orders")
    public ResponseEntity<CommonResponse> createOrder(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody PaymentOrderCreateRequest request) {

        PaymentOrderCreateResponse response = paymentService.createOrder(authUser.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("주문 생성을 성공했습니다.", response));
    }

    @Operation(summary = "토스페이 결제 승인")
    @PostMapping("/confirm")
    public ResponseEntity<CommonResponse> confirm(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody PaymentConfirmRequest request) {

        PaymentConfirmResponse response = paymentService.confirm(authUser.getId(), request);

        return ResponseEntity.status(HttpStatus.OK).body((CommonResponse.success("결제 승인을 성공했습니다.", response)));
    }

    @Operation(summary = "토스 clientKey 조회")
    @GetMapping("/config")
    public ResponseEntity<CommonResponse> config() {

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("토스 설정 조회 성공!", PaymentConfigResponse.from(tossClientKey)));
    }
}