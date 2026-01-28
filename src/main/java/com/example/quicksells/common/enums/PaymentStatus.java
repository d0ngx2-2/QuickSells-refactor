package com.example.quicksells.common.enums;

/**
 * v3 결제 상태
 *
 * - 포인트 충전만 다루는 최소 상태만 유지한다.
 * - 추후 환불/취소 도입 시 확장 가능하다.
 */
public enum PaymentStatus {

    /**
     * 결제 승인 완료 (포인트 충전 처리 가능)
     */
    APPROVED,

    /**
     * 결제 실패 (포인트 충전 불가)
     */
    FAILED
}
