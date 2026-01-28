package com.example.quicksells.domain.payment.entity;

import com.example.quicksells.common.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "payments",
        uniqueConstraints = {
                /**
                 * 주문 식별자는 외부 결제 시스템과의 통신 기준이므로 중복을 허용하지 않는다.
                 */
                @UniqueConstraint(name = "unique_payment_order_id", columnNames = "order_id"),

                /**
                 * 토스 결제 키는 결제 승인 이후 발급되는 고유 키로 중복을 허용하지 않는다.
                 */
                @UniqueConstraint(name = "unique_payment_key", columnNames = "payment_key")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    /**
     * 내부 결제 식별자 (데이터베이스 기본키)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 결제를 요청한 사용자 식별자
     *
     * - 포인트 충전은 사용자에게 귀속된다.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 외부 결제 시스템(토스) 기준 주문 식별자
     *
     * - 토스 결제 요청/승인 과정에서 "문자열" 주문 식별자를 사용한다.
     * - 로그 추적과 중복 결제 방지를 위해 문자열을 사용한다.
     */
    @Column(name = "order_id", nullable = false, length = 100)
    private String orderId;

    /**
     * 토스에서 발급하는 결제 고유 키
     *
     * - 결제 승인이 완료되면 발급된다.
     */
    @Column(name = "payment_key", nullable = false, length = 200)
    private String paymentKey;

    /**
     * 결제 금액 (원 단위)
     *
     * - 포인트 충전 금액과 동일하게 사용한다.
     */
    @Column(name = "amount", nullable = false)
    private Integer amount;

    /**
     * 결제 상태 (v3: 충전 기준으로 최소화)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    private PaymentStatus paymentStatus;

    /**
     * 결제 승인 시각 (승인 완료 시점에 세팅)
     */
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    /**
     * 결제 생성 시각
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Payment(Long userId, String orderId, String paymentKey, Integer amount, PaymentStatus paymentStatus) {
        this.userId = userId;
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 결제 승인 완료 처리
     */
    public void markAsApproved() {
        this.paymentStatus = PaymentStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    /**
     * 결제 실패 처리
     */
    public void markAsFailed() {
        this.paymentStatus = PaymentStatus.FAILED;
    }
}
