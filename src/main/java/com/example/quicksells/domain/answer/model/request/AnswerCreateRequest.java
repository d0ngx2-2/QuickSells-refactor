package com.example.quicksells.domain.answer.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateRequest {

    @NotBlank
    @Size(max = 500)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String content;
}
