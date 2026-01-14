package com.example.quicksells.domain.auction.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AuctionCreateResponse {

    private final Long id;
    private final Long appraiseId;
    private final Long dealId;
    private final Integer bidPrice;
    private final String status;
    private final LocalDateTime createdAt;
}
