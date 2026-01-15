package com.example.quicksells.domain.item.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemUpdateRequest {
    private String name;
    private Long hopePrice;
    private String description;
    private String image;

    public ItemUpdateRequest(String name,
                             Long hopePrice,
                             String description,
                             String image) {
        this.name = name;
        this.hopePrice = hopePrice;
        this.description = description;
        this.image = image;
    }
}
