package com.example.quicksells.domain.auction.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionSearchFilterRequest {

    private Integer minBidPrice; // between 최소 입찰가격
    private Integer maxBidPrice; // between 최대 입찰가격
    private String appraiseItemName; // 감정된 아이템 이름
}
