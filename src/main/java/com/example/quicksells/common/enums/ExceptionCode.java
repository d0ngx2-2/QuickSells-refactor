package com.example.quicksells.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    //appraise
    NOT_FOUND_APPRAISE(HttpStatus.NOT_FOUND, "감정을 찾을 수 없습니다."), //auction
    NOT_FOUND_APPRAISER(HttpStatus.NOT_FOUND, "감정사를 찾을 수 없습니다."),
    NOT_APPRAISE_ITEM_DELETE(HttpStatus.CONFLICT, "삭제된 상품에는 감정을 할 수 없습니다."),
    EXISTS_ITEM_SELL(HttpStatus.CONFLICT, "이미 판매 완료된 상품입니다."),
    ONLY_OWNER_APPRAISE_SEARCH(HttpStatus.FORBIDDEN, "본인의 상품에 대한 감정만 조회할 수 있습니다."),
    NOT_DELETE_SELECTED_APPRAISE(HttpStatus.CONFLICT, "이미 선택된 감정은 삭제가 불가합니다."),
    EXISTS_ALREADY_SELECT_APPRAISE(HttpStatus.CONFLICT, "이미 선택된 다른 감정이 존재합니다."),
    ALREADY_SELECT_APPRAISE(HttpStatus.CONFLICT, "이미 선택된 감정입니다."),

    //auction
    NOT_FOUND_AUCTION(HttpStatus.NOT_FOUND, "경매 정보를 찾을 수 없습니다."),
    NOT_FOUND_DEAL(HttpStatus.NOT_FOUND, "거래를 찾을 수 없습니다."), //Auction, Deal
    BID_PRICE_TOO_LOW(HttpStatus.BAD_REQUEST, "현재 입찰가보다 더 높은 금액을 입력해야 합니다."),
    ACCESS_DENIED_ONLY_OWNER(HttpStatus.FORBIDDEN, "경매에 대한 권한이 없습니다."),
    AUCTION_ALREADY_EXPIRED(HttpStatus.BAD_REQUEST, "해당 경매가 종료되어 삭제되었습니다."),

    //deal
    NOT_DEAL_ON_SALE(HttpStatus.NOT_FOUND, "거래 중 상태가 아닙니다."),//deal entity

    //auth
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다."),
    NOT_MATCHES_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    EXISTS_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),

    //USER
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."), //auction,Deal,User,Item
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    EXISTS_PHONE(HttpStatus.CONFLICT, "이미 존재하는 핸드폰 번호입니다."), //auth,User
    NO_UPDATE_FIELD(HttpStatus.BAD_REQUEST, "수정할 정보가 없습니다"),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 권한입니다."), //UserRole
    CONFLICT_AUCTION(HttpStatus.CONFLICT, "중복된 경매입니다."),

    //ITEM
    NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "상품를 찾을 수 없습니다."), //appraise,Deal,item
    ACCESS_DENIED_EXCEPTION_UPDATED_ITEM(HttpStatus.FORBIDDEN, "상품 수정 권한이 없습니다."),
    ACCESS_DENIED_EXCEPTION_DELETED_ITEM(HttpStatus.FORBIDDEN, "상품 삭제 권한이 없습니다."),
    CONFLICT_ITEM(HttpStatus.CONFLICT, "중복된 상품입니다."),

    //미사용
    AUCTION_EXPIRED_SOLD_OUT(HttpStatus.BAD_REQUEST, "경매 시간이 종료되어 낙찰이 완료 되었습니다."),
    AUCTION_EXPIRED_UNSOLD(HttpStatus.BAD_REQUEST, "경매시간이 종료되었으나 낙찰자가 없습니다."),
    NOT_DEAL_BEFORE(HttpStatus.NOT_FOUND, "거래 전 상태가 아닙니다."),
    ALREADY_DELETE_APPRAISE(HttpStatus.CONFLICT, "이미 삭제된 감정입니다."),
    NULL_POINT_BID_PRICE(HttpStatus.INTERNAL_SERVER_ERROR, "입찰 금액을 작성해주세요."),
    ;

    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
