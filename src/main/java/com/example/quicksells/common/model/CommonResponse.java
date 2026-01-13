package com.example.quicksells.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommonResponse {

    private final boolean success;
    private final String message;
    private final Object data;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public CommonResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // 성공 응답 (데이터 없음)
    public static CommonResponse success(String message) {
        return new CommonResponse(true, message, null);
    }

    // 성공 응답 (커스텀 메시지)
    public static CommonResponse success(String message, Object data) {
        return new CommonResponse(true, message, data);
    }

    // 실패 응답
    public static CommonResponse error(String message) {
        return new CommonResponse(false, message, null);
    }

    // 실패 응답 (데이터 포함)
    public static CommonResponse error(String message, Object data) {
        return new CommonResponse(false, message, data);
    }
}
