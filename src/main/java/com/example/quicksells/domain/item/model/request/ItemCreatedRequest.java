package com.example.quicksells.domain.item.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ItemCreatedRequest {
    @NotNull(message = "상품이름은 필수 입니다.")
    private String name;

    @NotNull
    private Long hopePrice;
    private String description;
    private String image;

    public ItemCreatedRequest(String name,
                              Long hopePrice,
                              String description,
                              String image) {
        this.name = name;
        this.hopePrice = hopePrice;
        this.description = description;
        this.image = image;
    }
}
