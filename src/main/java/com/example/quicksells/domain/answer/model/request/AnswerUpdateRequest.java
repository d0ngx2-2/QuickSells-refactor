package com.example.quicksells.domain.answer.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerUpdateRequest {

    @NotBlank(message = "수정할 답변 제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "수정할 답변 내용을 입력해 주세요.")
    private String content;
}
