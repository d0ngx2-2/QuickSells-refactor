package com.example.quicksells.domain.appraise.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppraiseCreateRequest {

    @NotNull(message = "감정가는 필수입니다.")
    @Min(value = 1, message = "감정가는 1원 이상이어야 합니다.")
    private Integer bidPrice;
}
