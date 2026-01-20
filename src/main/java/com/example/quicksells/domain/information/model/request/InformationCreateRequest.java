package com.example.quicksells.domain.information.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InformationCreateRequest {

    @NotBlank(message = "공지사항 제목은 필수입니다.")
    @Size(max = 50, message = "공지사항 제목은 최대 50자까지 입력할 수 있습니다.")
    private String title;

    @NotBlank(message = "공지사항 내용은 필수입니다.")
    @Size(max = 500, message = "공지사항 내용은 최대 500자까지 입력할 수 있습니다.")
    private String description;
}
