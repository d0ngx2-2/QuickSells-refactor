package com.example.quicksells.domain.payment.entity;

import com.example.quicksells.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments", uniqueConstraints = {@UniqueConstraint(name = "unique_key_payment_order_id", columnNames = "order_id"), @UniqueConstraint(name = "unique_key_payment_key", columnNames = "toss_payment_key")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "toss_payment_key", length = 200)
    private String tossPaymentKey;

    @Column(name = "order_id", nullable = false, length = 100)
    private String orderId;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static Payment ready(Long userId, String orderId, Long amount) {
        Payment p = new Payment();
        p.userId = userId;
        p.orderId = orderId;
        p.amount = amount;
        p.status = PaymentStatus.READY;

        return p;
    }

    public void markDone(String tossPaymentKey, LocalDateTime approvedAt) {
        this.tossPaymentKey = tossPaymentKey;
        this.approvedAt = approvedAt;
        this.status = PaymentStatus.DONE;
    }

    public void markCanceled() {
        this.status = PaymentStatus.CANCELED;
    }

    public void markFailed() {
        this.status = PaymentStatus.FAILED;
    }
}
