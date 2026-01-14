package com.example.quicksells.domain.item.dto.request;

import lombok.Getter;

@Getter
public class ItemCreatedRequest {
    private String name;
    private Long hopePrice;
    private String description;
    private String image;

    public ItemCreatedRequest(String name, Long hopePrice, String description, String image) {
        this.name = name;
        this.hopePrice = hopePrice;
        this.description = description;
        this.image = image;
    }
}
