package com.example.quicksells.domain.item.dto.response;

import com.example.quicksells.domain.item.dto.dto.ItemDto;
import com.example.quicksells.domain.item.dto.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemGetDetailResponse {
    private final Long id;
    private final Long userId;
    private final UserInfoDto user;

    public static ItemGetDetailResponse from(ItemDto dto) {
        return new ItemGetDetailResponse(dto.getId(), dto.getUserId(),
                new UserInfoDto(dto.getName(), dto.getHopePrice(), dto.getDescription(), dto.getImage(), dto.isStatus(), dto.getRole(), dto.getCreatedAt()));
    }
}
