package com.example.quicksells.domain.information.model.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.repository.NoRepositoryBean;

@Getter
@AllArgsConstructor
@NoRepositoryBean
public class InformationUpdateRequest {

    @Size(max = 50, message = "공지사항 제목은 최대 50자까지 입력할 수 있습니다.")
    private String title;

    @Size(max = 500, message = "공지사항 내용은 최대 500자까지 입력할 수 있습니다.")
    private String description;

    private Boolean deleteImage;

    public boolean isAllFieldEmpty() {return title == null && description == null && deleteImage == null;}
}
