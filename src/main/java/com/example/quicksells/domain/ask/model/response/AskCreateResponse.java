package com.example.quicksells.domain.ask.model.response;

import com.example.quicksells.common.enums.AskType;
import com.example.quicksells.domain.ask.entity.Ask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AskCreateResponse {

    private final Long askId;
    private final Long userId;
    private final String userName;
    private final AskType askType;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // Entity -> DTO 변환
    public static AskCreateResponse from(Ask ask) {
        return new AskCreateResponse(
                ask.getId(),
                ask.getUser().getId(),
                ask.getUser().getName(),
                ask.getAskType(),
                ask.getTitle(),
                ask.getContent(),
                ask.getCreatedAt(),
                ask.getUpdatedAt());
    }
}
