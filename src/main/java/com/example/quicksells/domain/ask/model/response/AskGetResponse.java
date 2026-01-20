package com.example.quicksells.domain.ask.model.response;

import com.example.quicksells.common.enums.AskType;
import com.example.quicksells.domain.ask.entity.Ask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AskGetResponse {

    private final Long askId;
    private final Long userId;
    private final String userName;  // 본인 조회이므로 실제 이름
    private final AskType askType;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    // Entity -> DTO 변환
    public static AskGetResponse from(Ask ask) {
        return new AskGetResponse(
                ask.getId(),
                ask.getUser().getId(),
                ask.getUser().getName(),  // 실제 이름
                ask.getAskType(),
                ask.getTitle(),
                ask.getContent(),
                ask.getCreatedAt()
        );

    }

}
