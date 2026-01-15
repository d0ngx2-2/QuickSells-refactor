package com.example.quicksells.domain.auction.model.response;

import com.example.quicksells.domain.auction.entity.Auction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AuctionUpdateResponse {

    private final Long id;
    private final Long appraiseId;
    private final Long dealId;
    private final Long buyerId;
    private final Integer bidPrice;
    private final String status;
    private final LocalDateTime updatedAt;

    public static AuctionUpdateResponse from(Auction auction) {
        return new AuctionUpdateResponse(
                auction.getId(),
                auction.getAppraise().getId(),
                auction.getDeal().getId(),
                auction.getUser().getId(),
                auction.getBidPrice(),
                auction.getStatus(),
                auction.getUpdatedAt()
        );
    }
}
