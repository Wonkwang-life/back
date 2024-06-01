package com.wonkwang.health.exception;

import com.wonkwang.health.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.wonkwang.health.dto.ResponseEntityBuilder.build;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<?>> handleRuntimeException(RuntimeException ex) {
        log.info(ex.getMessage());
        return build(ex.getMessage(), BAD_REQUEST);
    }
}
