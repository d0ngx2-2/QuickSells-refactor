package com.example.quicksells.domain.auction.dto.request;

import lombok.Getter;

@Getter
public class AuctionUpdateRequest {

    private Long buyerId;
    private Integer bidPrice;
}
