package com.example.quicksells.domain.answer.model.response;

import com.example.quicksells.domain.answer.entity.Answer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AnswerCreateResponse {

    private final Long answerId;
    private final Long askId;
    private final Long adminId;
    private final String adminName;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    public static AnswerCreateResponse from(Answer answer) {
        return new AnswerCreateResponse(
                answer.getId(),
                answer.getAsk().getId(),
                answer.getAdmin().getId(),
                answer.getAdmin().getName(),
                answer.getTitle(),
                answer.getContent(),
                answer.getCreatedAt()
        );
    }
}
