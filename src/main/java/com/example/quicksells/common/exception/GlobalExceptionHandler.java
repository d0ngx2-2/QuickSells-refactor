package com.example.quicksells.common.exception;

import com.example.quicksells.common.enums.ExceptionCode;
import com.example.quicksells.common.model.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse> customException(CustomException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();

        CommonResponse response = new CommonResponse(false, exceptionCode.getMessage(), null);

        return ResponseEntity.status(exceptionCode.getStatus()).body(response);
    }

    //Valid 검증 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        CommonResponse response = new CommonResponse(false, message, null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 예상하지 못한 예외 처리 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleException(Exception e) {
        log.error("예상하지 못한 예외 발생: ", e);

        return ResponseEntity.internalServerError().body(CommonResponse.error("서버 내부 오류가 발생했습니다."));
    }
}
