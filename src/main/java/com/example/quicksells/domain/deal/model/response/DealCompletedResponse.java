package com.example.quicksells.domain.deal.model.response;

import com.example.quicksells.common.enums.DealType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class DealCompletedResponse {

    private Long dealId;
    private DealType dealType;          // IMMEDIATE_SELL | AUCTION
    private Integer dealPrice;          // 최종 판매가
    private Long itemId;
    private String itemName;
    private LocalDateTime completedAt;
}
