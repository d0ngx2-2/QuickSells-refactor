package com.example.quicksells.domain.auction.model.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionSearchFilterRequest {

    @Min(value = 1, message = "최소 입찰가는 1원 이상이어야 합니다.")
    private Integer minBidPrice; // between 최소 입찰가격
    @Min(value = 1, message = "최대 입찰가는 1원 이상이어야 합니다.")
    private Integer maxBidPrice; // between 최대 입찰가격
    private String appraiseItemName; // 감정된 아이템 이름
}
