package com.example.quicksells.domain.information.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InformationCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String imageUrl;
}
