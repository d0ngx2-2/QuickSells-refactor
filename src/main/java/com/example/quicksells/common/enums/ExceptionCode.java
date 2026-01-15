package com.example.quicksells.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    NOT_MATCHES_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    EXISTS_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    EXISTS_PHONE(HttpStatus.CONFLICT, "이미 존재하는 핸드폰 번호입니다."),

    NOT_FOUND_APPRAISE(HttpStatus.NOT_FOUND, "감정을 찾을 수 없습니다."),
    NOT_FOUND_DEAL(HttpStatus.NOT_FOUND, "거래를 찾을 수 없습니다."),
    NOT_FOUND_AUCTION(HttpStatus.NOT_FOUND, "경매를 찾을 수 없습니다."),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 권한입니다."),

    AUCTION_EXPIRED_SOLD_OUT(HttpStatus.BAD_REQUEST, "경매 시간이 종료되어 낙찰이 완료 되었습니다."),
    AUCTION_EXPIRED_UNSOLD(HttpStatus.BAD_REQUEST, "경매시간이 종료되었으나 낙찰자가 없습니다."),

    NULL_POINT_BID_PRICE(HttpStatus.INTERNAL_SERVER_ERROR, "입찰 금액을 작성해주세요."),
    BID_PRICE_TOO_LOW(HttpStatus.BAD_REQUEST, "현재 입찰가보다 더 높은 금액을 입력해야 합니다."),
    AUCTION_ALREADY_EXPIRED(HttpStatus.BAD_REQUEST, "해당 경매가 종료되어 삭제되었습니다.")
    ;
    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
