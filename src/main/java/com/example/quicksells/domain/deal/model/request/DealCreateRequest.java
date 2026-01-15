package com.example.quicksells.domain.deal.model.request;

import com.example.quicksells.common.enums.DealType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DealCreateRequest {

    private Long itemId;
    private Long buyerId;
    private DealType type;
    private Integer dealPrice;
}
