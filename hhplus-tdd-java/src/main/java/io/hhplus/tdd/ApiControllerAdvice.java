package io.hhplus.tdd;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomException e) {
        return ResponseEntity.status(500).body(new ErrorResponse(e.getStatusCode(), e.getMsg()));
    }
}
