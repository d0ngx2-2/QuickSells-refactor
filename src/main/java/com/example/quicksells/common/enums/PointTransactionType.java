package com.example.quicksells.common.enums;

/**
 * v3 포인트 거래 유형
 *
 * 정책:
 * - 충전(+) : 결제 승인 후 지갑 증가
 * - 낙찰 차감(-) : 경매 종료 후 낙찰자 지갑 차감
 * - 판매자 적립(+) : 낙찰 금액을 판매자 지갑에 적립
 */
public enum PointTransactionType {

    /**
     * 포인트 충전
     */
    CHARGE,

    /**
     * 낙찰 성공으로 인한 포인트 차감(구매자)
     */
    AUCTION_WIN_DEDUCT,

    /**
     * 낙찰 성공으로 인한 포인트 적립(판매자)
     */
    AUCTION_SELLER_CREDIT
}
