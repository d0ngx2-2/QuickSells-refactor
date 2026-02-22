package com.example.quicksells.domain.information.model.response;

import com.example.quicksells.domain.information.entity.Information;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InformationGetResponse {

    private final Long id;
    private final Long adminId;
    private final String title;
    private final String description;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InformationGetResponse from(Information information) {
        return new InformationGetResponse(
                information.getId(),
                information.getUser().getId(),
                information.getTitle(),
                information.getDescription(),
                information.getImageUrl(),
                information.getCreatedAt(),
                information.getUpdatedAt());
    }
}
