package com.example.quicksells.domain.item.dto.response;

import com.example.quicksells.domain.item.dto.dto.ItemInfoDto;
import com.example.quicksells.domain.item.entity.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemGetListResponse {
    private final Long id;
    private final Long userId;
    private final ItemInfoDto user;

    public static ItemGetListResponse from(Item item) {
        return new ItemGetListResponse(
                item.getId(),
                item.getUser().getId(),
                new ItemInfoDto(
                        item.getName(),
                        item.getHopePrice(),
                        item.getDescription(),
                        item.getImage(),
                        item.isStatus(),
                        item.getUser().getRole(),
                        item.getCreatedAt()));
    }
}
