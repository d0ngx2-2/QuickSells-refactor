package com.example.quicksells.domain.deal.model.response;

import com.example.quicksells.common.enums.DealType;
import com.example.quicksells.common.enums.StatusType;
import com.example.quicksells.domain.deal.entity.Deal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class DealListResponse {

    private final Long dealId;
    private final DealType type;
    private final StatusType status;
    private final Integer dealPrice;
    private final LocalDateTime createdAt;

    private final Long userId;     // 구매 or 판매 주체
    private final String userName; // 구매자 or 판매자 이름

    public static DealListResponse forPurchase(Deal deal) {
        return new DealListResponse(
                deal.getId(),
                deal.getType(),
                deal.getStatus(),
                deal.getDealPrice(),
                deal.getCreatedAt(),
                deal.getBuyer().getId(),
                deal.getBuyer().getName()
        );
    }

    public static DealListResponse forSale(Deal deal) {
        return new DealListResponse(
                deal.getId(),
                deal.getType(),
                deal.getStatus(),
                deal.getDealPrice(),
                deal.getCreatedAt(),
                deal.getSeller().getId(),
                deal.getSeller().getName()
        );
    }
}

