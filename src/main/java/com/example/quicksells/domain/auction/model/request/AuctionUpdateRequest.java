package com.example.quicksells.domain.auction.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AuctionUpdateRequest {

    @NotNull(message = "구매자는 필수입니다.")
    private Long buyerId;

    @NotNull(message = "입찰 가격을 입력해주세요.")
    private Integer bidPrice;
}
