package com.example.quicksells.domain.appraise.model.response;

import com.example.quicksells.common.enums.AppraiseStatus;
import com.example.quicksells.common.enums.StatusType;
import com.example.quicksells.domain.appraise.entity.Appraise;
import com.example.quicksells.domain.deal.entity.Deal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AppraiseImmediateSellResponse {

    // 감정 정보
    private final Long appraiseId;
    private final Integer appraisedPrice;  // 감정가
    private final AppraiseStatus appraiseStatus;  // IMMEDIATE_SELL

    // 거래 정보
    private final Long dealId;
    private final StatusType dealStatus;  // ON_SALE
    private final Integer dealPrice;  // 최종 거래 가격

    // 상품 정보 (간단하게)
    private final Long itemId;
    private final String itemName;

    public static AppraiseImmediateSellResponse from(Appraise appraise, Deal deal) {
        return new AppraiseImmediateSellResponse(
                appraise.getId(),
                appraise.getBidPrice(),
                appraise.getAppraiseStatus(),
                deal.getId(),
                deal.getStatus(),
                deal.getDealPrice(),
                appraise.getItem().getId(),
                appraise.getItem().getName()
        );

    }
}
