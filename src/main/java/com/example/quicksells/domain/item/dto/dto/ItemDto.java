package com.example.quicksells.domain.item.dto.dto;

import com.example.quicksells.common.enums.UserRole;
import com.example.quicksells.domain.item.entity.Item;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemDto {
    private final Long id;
    private final Long userId;
    private final String name;
    private final Long hopePrice;
    private final String description;
    private final String image;
    private final boolean status;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ItemDto(Long id, Long userId, String name, Long hopePrice, String description, String image, boolean status, UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.userId = userId;
        this.name = name;
        this.hopePrice = hopePrice;
        this.description = description;
        this.image = image;
        this.status = status;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

//    public static ItemDto from(Item item) {
//        return new ItemDto(item.getId(), item.getUser().getId(), item.getName(), item.getHopePrice(), item.getDescription(), item.getImage(), item.isStatus(), item.getUser().getRole(), item.getCreatedAt(), item.getUpdatedAt());
//    }
}
