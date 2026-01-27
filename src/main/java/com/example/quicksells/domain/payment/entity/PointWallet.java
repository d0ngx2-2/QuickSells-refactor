package com.example.quicksells.domain.payment.entity;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.exception.CustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point_wallets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointWallet {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private Long balance;        // 현 잔액 포인트

    @Version
    private Long version;        // 낙찰 / 즉시구매 동시 차감 방지

    protected PointWallet(Long userId) {
        this. userId = userId;
        this.balance = 0L;
    }

    public static PointWallet create(Long userId) {
        return new PointWallet(userId);
    }

    public void charge(long amount) {
        if (amount <= 0) throw new CustomException(ExceptionCode.AMOUNT_MUST_BE_POSITIVE);
        this.balance += amount;
    }

    public void withdraw(long amount) {
        if (amount <= 0) throw new CustomException(ExceptionCode.AMOUNT_MUST_BE_POSITIVE);
        if (this.balance < amount) throw new CustomException(ExceptionCode.INSUFFICIENT_POINT);
        this.balance -= amount;
    }
}
