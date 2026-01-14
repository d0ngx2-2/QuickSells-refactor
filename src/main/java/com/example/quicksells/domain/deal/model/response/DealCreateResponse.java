package com.example.quicksells.domain.deal.model.response;

import com.example.quicksells.common.enums.DealType;
import com.example.quicksells.domain.deal.entity.Deal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DealCreateResponse {

    private final Long dealId;
    private final Integer dealPrice;
    private final DealType status;

    public static DealCreateResponse from(Deal deal) {
        return new DealCreateResponse(deal.getId(), deal.getDealPrice(), deal.getType());
    }
}
