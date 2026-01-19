package com.example.quicksells.domain.ask.model.response;

import com.example.quicksells.common.enums.AskType;
import com.example.quicksells.domain.ask.entity.Ask;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AskCreateResponse {

    private Long askId;
    private Long userId;
    private String userName;
    private AskType askType;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
