package com.mzherdev.tictactoe.exception;

import com.mzherdev.tictactoe.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return processException(e, e.getMessage(),  HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalMoveException.class)
    public ResponseEntity<Object> handleIllegalMoveException(IllegalMoveException e) {
        return processException(e, e.getMessage(),  HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return processException(e, e.getMessage(),  HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return processException(e, e.getMessage(),  HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<Object> processException(Exception e, String message, HttpStatus status) {
        log.info("caught exception with status: {} and message: {}", status, message, e);
        return new ResponseEntity<>(new ApiError(message), status);
    }


}
