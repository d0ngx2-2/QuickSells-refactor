package com.example.quicksells.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    EXISTS_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    EXISTS_PHONE(HttpStatus.CONFLICT, "이미 존재하는 핸드폰 번호입니다."),

    NOT_FOUND_APPRAISE(HttpStatus.NOT_FOUND, "감정을 찾을 수 없습니다."),
    NOT_FOUND_DEAL(HttpStatus.NOT_FOUND, "거래를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
