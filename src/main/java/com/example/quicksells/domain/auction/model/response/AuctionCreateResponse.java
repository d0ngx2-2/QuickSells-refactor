package com.example.quicksells.domain.auction.model.response;

import com.example.quicksells.common.enums.AuctionStatusType;
import com.example.quicksells.domain.auction.entity.Auction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AuctionCreateResponse {

    private final Long id;
    private final Long appraiseId;
    private final Long sellerId;
    private final String itemName;
    private final Integer bidPrice;
    private final AuctionStatusType status;
    private final LocalDateTime createdAt;

    public static AuctionCreateResponse from(Auction auction) {
        return new AuctionCreateResponse(
                auction.getId(),
                auction.getAppraise().getId(),
                auction.getAppraise().getItem().getUser().getId(),
                auction.getAppraise().getItem().getName(),
                auction.getBidPrice(),
                auction.getStatus(),
                auction.getCreatedAt()
        );
    }
}

