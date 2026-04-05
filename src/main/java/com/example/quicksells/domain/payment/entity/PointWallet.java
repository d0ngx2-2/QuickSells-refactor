package com.example.quicksells.domain.payment.entity;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "point_wallets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointWallet {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "available_balance", nullable = false)
    private Long availableBalance;

    @Version
    @Column(nullable = false)
    private Long version;

    public PointWallet(Long userId) {
        this.userId = userId;
        this.availableBalance = 0L;
    }

    public void increaseBalance(long amount) {
        if (amount <= 0) {
            throw new CustomException(ExceptionCode.INVALID_CHARGE_AMOUNT);
        }
        this.availableBalance += amount;
    }

    public void decreaseBalance(long amount) {
        if (amount <= 0) {
            throw new CustomException(ExceptionCode.INVALID_PAYMENT_AMOUNT);
        }
        if (this.availableBalance < amount) {
            throw new CustomException(ExceptionCode.INSUFFICIENT_BALANCE);
        }
        this.availableBalance -= amount;
    }
}