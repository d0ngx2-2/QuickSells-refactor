package com.example.quicksells.domain.deal.model.response;

import com.example.quicksells.common.enums.DealType;
import com.example.quicksells.common.enums.StatusType;
import com.example.quicksells.domain.deal.entity.Deal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DealGetResponse {

    private final Long dealId;
    private final DealType type;
    private final StatusType status;
    private final Integer dealPrice;
    private final LocalDateTime createdAt;

    public static DealGetResponse from(Deal deal) {
        return new DealGetResponse(deal.getId(), deal.getType(), deal.getStatus(), deal.getDealPrice(), deal.getCreatedAt());
    }
}
