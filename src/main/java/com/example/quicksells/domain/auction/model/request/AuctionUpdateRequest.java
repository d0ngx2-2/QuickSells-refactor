package com.example.quicksells.domain.auction.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuctionUpdateRequest {

    private Long buyerId;
    @NotBlank(message = "입찰 가격을 입력해주세요.")
    private Integer bidPrice;
}
