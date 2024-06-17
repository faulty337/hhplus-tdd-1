package io.hhplus.tdd;

public record ErrorResponse(
        int code,
        String message
) {
}
