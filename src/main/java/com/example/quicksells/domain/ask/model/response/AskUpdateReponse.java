package com.example.quicksells.domain.ask.model.response;

import com.example.quicksells.common.enums.AskType;
import com.example.quicksells.domain.ask.entity.Ask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AskUpdateReponse {

    private final Long askId;
    private final AskType askType;
    private final String title;
    private final String content;
    private final LocalDateTime updatedAt;

    // Entity -> DTO 변환
    public static AskUpdateReponse from(Ask ask) {
        return new AskUpdateReponse(
                ask.getId(),
                ask.getAskType(),
                ask.getTitle(),
                ask.getContent(),
                ask.getUpdatedAt()
        );

    }
}
