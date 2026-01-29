package com.example.quicksells.domain.chat.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 메시지 전송 요청")
public class ChatMessageRequest {

    @NotBlank(message = "메시지 내용은 필수입니다")
    @Schema(description = "메시지 내용", example = "안녕하세요")
    private String content;
}
