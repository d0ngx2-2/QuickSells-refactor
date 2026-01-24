package com.example.quicksells.domain.deal.model.request;

import com.example.quicksells.common.enums.DealType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DealCreateRequest {

    @NotNull(message = "감정 ID는 필수입니다.")
    private Long appraiseId;

    @NotNull(message = "거래 유형은 필수입니다.")
    private DealType type;   // IMMEDIATE_SELL / AUCTION

    @NotNull(message = "거래 가격은 필수입니다.")
    private Integer dealPrice;
}
