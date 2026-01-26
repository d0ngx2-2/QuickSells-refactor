package com.example.quicksells.domain.appraise.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppraiseAuctionProceedRequest {

    @NotNull(message = "경매 종료 시간을 입력해주세요.")
    @Min(value = 1, message = "경매 종료 시간은 최소 1일부터 선택가능합니다.")
    @Max(value = 3, message = "경매 종료 시간은 최대 3일까지 선택가능합니다.")
    private Integer timeOption;
}
