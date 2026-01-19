package com.example.quicksells.domain.information.model.response;

import com.example.quicksells.domain.information.entity.Information;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InformationUpdateResponse {

    private final Long id;
    private final Long adminId;
    private final String title;
    private final String description;
    private final String imageUrl;
    private final LocalDateTime updatedAt;

    public static InformationUpdateResponse from(Information information) {
        return new InformationUpdateResponse(
                information.getId(),
                information.getUser().getId(),
                information.getTitle(),
                information.getDescription(),
                information.getImageUrl(),
                information.getUpdatedAt());
    }
}
