package com.example.quicksells.domain.payment.service;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.enums.PointTransactionType;
import com.example.quicksells.common.enums.StatusType;
import com.example.quicksells.common.exception.CustomException;
import com.example.quicksells.domain.auction.entity.Auction;
import com.example.quicksells.domain.deal.entity.Deal;
import com.example.quicksells.domain.deal.repository.DealRepository;
import com.example.quicksells.domain.payment.model.TransactionReference;
import com.example.quicksells.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionSettlementService {

    private final DealRepository dealRepository;
    private final PointLedgerService pointLedgerService;

    @Transactional
    public void settleSuccessfulAuction(Auction auction) {

        User buyer = auction.getBuyer();
        if (buyer == null) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }

        User seller = auction.getAppraise().getItem().getSeller();
        if (seller == null) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }

        long amount = auction.getBidPrice().longValue();
        if (amount <= 0) {
            throw new CustomException(ExceptionCode.INVALID_CHARGE_AMOUNT);
        }

        Deal deal = dealRepository.findByAppraiseId(auction.getAppraise().getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_DEAL));

        if (deal.getStatus() == StatusType.SOLD) {
            log.info("[AuctionSettlement] 이미 정산 완료됨. auctionId={}, dealId={}", auction.getId(), deal.getId());
            return;
        }

        pointLedgerService.transfer(buyer.getId(), seller.getId(), amount, PointTransactionType.AUCTION_WIN_DEDUCT, PointTransactionType.AUCTION_SELLER_CREDIT, TransactionReference.ofAuction(auction.getId()));

        deal.completeAuction(auction.getBidPrice());

        log.info("[AuctionSettlement] 정산 완료. auctionId={}, amount={}, buyerId={}, sellerId={}", auction.getId(), amount, buyer.getId(), seller.getId());
    }
}