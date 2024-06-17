package io.hhplus.tdd;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public int getStatusCode() {
        return errorCode.getStatusCode().value();
    }

    public String getMsg(){
        return errorCode.getMsg();
    }
}