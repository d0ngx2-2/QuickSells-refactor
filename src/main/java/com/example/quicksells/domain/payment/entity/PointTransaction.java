package com.example.quicksells.domain.payment.entity;

import com.example.quicksells.common.enums.PointTransactionReferenceType;
import com.example.quicksells.common.enums.PointTransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_transactions", indexes = {@Index(name = "index_point_transaction_user_created", columnList = "user_id, create_at")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PointTransactionType type;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "balance_after", nullable = false)
    private Long balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", length = 20)
    private PointTransactionReferenceType referenceType;

    @Column(name = "reference_id")
    private  Long referenceId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static PointTransaction of(Long userId, PointTransactionType type, Long amount, Long balanceAfter, PointTransactionReferenceType referenceType, Long referenceId) {
        PointTransaction pt = new PointTransaction();
        pt.userId = userId;
        pt.type = type;
        pt.amount = amount;
        pt.balanceAfter = balanceAfter;
        pt.referenceType = referenceType;
        pt.referenceId = referenceId;

        return pt;
    }
}
