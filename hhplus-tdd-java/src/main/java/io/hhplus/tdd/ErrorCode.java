package io.hhplus.tdd;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저 ID를 찾지 못했습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 타입의 파라미터 입력입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 입니다."),
    INSUFFICIENT_FUNDS(HttpStatus.BAD_REQUEST, "포인트가 적습니다."),
    OVERUSE(HttpStatus.BAD_REQUEST, "포인트가 부족합니다.");



    private final HttpStatus statusCode;
    private final String msg;

    ErrorCode(HttpStatus statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }

}
