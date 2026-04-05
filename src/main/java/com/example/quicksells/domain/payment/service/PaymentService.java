package com.example.quicksells.domain.payment.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.PaymentStatus;
import com.example.quicksells.common.enums.PointTransactionType;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.payment.entity.Payment;
import com.example.quicksells.domain.payment.entity.PointWallet;
import com.example.quicksells.domain.payment.model.TransactionReference;
import com.example.quicksells.domain.payment.model.request.PaymentConfirmRequest;
import com.example.quicksells.domain.payment.model.request.PaymentOrderCreateRequest;
import com.example.quicksells.domain.payment.model.response.PaymentConfirmResponse;
import com.example.quicksells.domain.payment.model.response.PaymentOrderCreateResponse;
import com.example.quicksells.domain.payment.repository.PaymentRepository;
import com.example.quicksells.domain.payment.toss.TossConfirmRequest;
import com.example.quicksells.domain.payment.toss.TossPaymentsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PointLedgerService pointLedgerService;
    private final PointWalletService pointWalletService;
    private final TossPaymentsClient tossPaymentsClient;

    @Transactional
    public PaymentOrderCreateResponse createOrder(Long userId, PaymentOrderCreateRequest request) {
        validateCreateAmount(request.getAmount());

        String orderId = UUID.randomUUID().toString();

        Payment payment = new Payment(userId, orderId, request.getAmount());
        paymentRepository.save(payment);

        return PaymentOrderCreateResponse.from(payment);
    }

    @Transactional
    public PaymentConfirmResponse confirm(Long userId, PaymentConfirmRequest request) {
        Payment payment = paymentRepository.findByOrderIdAndUserId(request.getOrderId(), userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_PAYMENT));

        validateConfirmAmount(payment, request);

        if (payment.getPaymentStatus() == PaymentStatus.APPROVED) {
            PointWallet wallet = pointWalletService.getOrCreate(userId);
            return PaymentConfirmResponse.from(payment, wallet);
        }

        if (payment.getPaymentStatus() != PaymentStatus.READY) {
            throw new CustomException(ExceptionCode.DUPLICATE_PAYMENT_KEY);
        }

        try {
            TossConfirmRequest tossRequest =
                    new TossConfirmRequest(
                            request.getPaymentKey(),
                            request.getOrderId(),
                            request.getAmount()
                    );

            tossPaymentsClient.confirm(tossRequest);

        } catch (Exception e) {
            throw new CustomException(ExceptionCode.TOSS_CONFIRM_FAILED);
        }

        try {
            payment.markAsApproved(request.getPaymentKey());

            PointWallet wallet = pointLedgerService.credit(
                    userId,
                    payment.getAmount().longValue(),
                    PointTransactionType.CHARGE,
                    TransactionReference.ofPayment(payment.getId())
            );

            return PaymentConfirmResponse.from(payment, wallet);

        } catch (Exception e) {
            payment.markAsFailed("내부 포인트 반영 실패: " + e.getMessage());

            try {
                tossPaymentsClient.cancel(
                        request.getPaymentKey(),
                        "내부 포인트 반영 실패로 인한 자동 취소"
                );
            } catch (Exception cancelException) {
                throw new CustomException(ExceptionCode.TOSS_CANCEL_FAILED);
            }

            throw e;
        }
    }

    private void validateCreateAmount(Integer amount) {
        if (amount == null || amount < 10000) {
            throw new CustomException(ExceptionCode.INVALID_CHARGE_AMOUNT);
        }
    }

    private void validateConfirmAmount(Payment payment, PaymentConfirmRequest request) {
        if (!payment.getAmount().equals(request.getAmount())) {
            throw new CustomException(ExceptionCode.INVALID_PAYMENT_AMOUNT);
        }
    }
}