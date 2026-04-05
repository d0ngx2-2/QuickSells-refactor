package com.example.quicksells.domain.payment.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class    TransactionReference {

    private final Long paymentId;
    private final Long auctionId;
    private final Long dealId;

    public static TransactionReference none() {
        return new TransactionReference(null, null, null);
    }

    public static TransactionReference ofPayment(Long paymentId) {
        return new TransactionReference(paymentId, null, null);
    }

    public static TransactionReference ofAuction(Long auctionId) {
        return new TransactionReference(null, auctionId, null);
    }

    public static TransactionReference ofDeal(Long dealId) {
        return new TransactionReference(null, null, dealId);
    }
}