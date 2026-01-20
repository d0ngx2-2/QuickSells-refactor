package com.example.quicksells.domain.appraise.model.response;

import com.example.quicksells.domain.appraise.entity.Appraise;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AppraiseCreateResponse {

    private final Long appraiseId;
    private final Long itemId;
    private final String itemName;

    // 감정사 정보
    private final Long adminId;
    private final String adminName;
    private final Integer bidPrice;
    private final Boolean isSelected;
    private final LocalDateTime createdAt;

    // Entity -> DTO 변환
    public static AppraiseCreateResponse from(Appraise appraise) {
        return new AppraiseCreateResponse(
                appraise.getId(),
                appraise.getItem().getId(),
                appraise.getItem().getName(),
                appraise.getUser().getId(),
                appraise.getUser().getName(),
                appraise.getBidPrice(),
                appraise.isSeleted(),
                appraise.getCreatedAt());
    }
}
