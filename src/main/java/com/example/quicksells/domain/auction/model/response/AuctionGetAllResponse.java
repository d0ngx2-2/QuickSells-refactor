package com.example.quicksells.domain.auction.model.response;

import com.example.quicksells.common.enums.AuctionStatusType;
import com.example.quicksells.domain.auction.entity.Auction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AuctionGetAllResponse {

    private final Long id;
    private final Long appraiseId;
    private final Long dealId;
    private final Long buyerId;
    private final Integer bidPrice;
    private final AuctionStatusType status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static AuctionGetAllResponse from(Auction auction) {
        return new AuctionGetAllResponse(
                auction.getId(),
                auction.getAppraise().getId(),
                auction.getDeal().getId(),
                auction.getBuyer() != null ? auction.getBuyer().getId() : null,// 입찰자가 존재하지 않으면 널을 반환
                auction.getBidPrice(),
                auction.getStatus(),
                auction.getCreatedAt(),
                auction.getUpdatedAt()
        );
    }
}
