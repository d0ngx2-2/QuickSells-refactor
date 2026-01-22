package com.example.quicksells.domain.item.model.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class ItemCreatedRequest {
    @NotBlank(message = "상품명은 필수 입니다.")
    @Size(max = 50, message = "상품명은 50자 이내입니다.")
    private String name;

    @NotNull(message = "상품 희망 가격은 필수입니다.")
    @Min(value = 1, message = "상품 희망 가격은 1원 이상이어야 합니다.")
    private Long hopePrice;

    @NotBlank(message = "상품 설명은 필수입니다.")
    @Size(max = 500, message = "상품 설명은 500자 이내입니다.")
    private String description;


    public ItemCreatedRequest(String name, Long hopePrice, String description) {
        this.name = name;
        this.hopePrice = hopePrice;
        this.description = description;

    }
}
