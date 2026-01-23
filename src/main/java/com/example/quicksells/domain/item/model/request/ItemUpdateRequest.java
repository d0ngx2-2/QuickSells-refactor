package com.example.quicksells.domain.item.model.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemUpdateRequest {

    @NotBlank(message = "상품명은 필수 입니다.")
    @Size(max = 255, message = "상품명은 255자 이내입니다.")
    private String name;

    @NotNull(message = "상품 희망 가격은 필수입니다.")
    @Min(value = 1, message = "상품 희망 가격은 1원 이상이어야 합니다.")
    private Long hopePrice;

    @NotBlank(message = "상품 설명은 필수입니다.")
    @Size(max = 500, message = "상품 설명은 500자 이내입니다.")
    private String description;

    private Boolean image;

    public ItemUpdateRequest(String name, Long hopePrice, String description, Boolean image) {
        this.name = name;
        this.hopePrice = hopePrice;
        this.description = description;
        this.image = image;
    }
}
