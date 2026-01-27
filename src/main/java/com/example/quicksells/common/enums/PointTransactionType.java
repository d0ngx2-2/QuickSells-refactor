package com.example.quicksells.common.enums;

public enum PointTransactionType {

    CHARGE,   // 포인트 충전 (증가)
    USE,      // 포인트 사용/차감 (감소) - 낙찰/즉시구매
    REFUND,   // 포인트 환불 (증가)
    WITHDRAW  // 포인트 출금 (감소)
}
