package sideproject.gugumo.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sideproject.gugumo.exception.exception.*;
import sideproject.gugumo.response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NotFoundException.class})
    public ApiResponse<String> handleNotFoundException(NotFoundException e) {
        log.error("[NotFoundException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getStatusCode());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {NoAuthorizationException.class})
    public ApiResponse<String> handleNoAuthorizationException(NoAuthorizationException e) {
        log.error("[handleNoAuthorizationException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {DuplicateResourceException.class})
    public ApiResponse<String> handleDuplicateResourceException(DuplicateResourceException e) {
        log.error("[DuplicateResourceException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getStatusCode());
    }

    /**
     * spring validation 예외 처리
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getFieldError().getDefaultMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> missingRequestHeaderExceptionHandler(MissingRequestHeaderException e) {
        String exceptionMessage = e.getMessage();
        log.error("[MissingRequestHeaderExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail("권한이 없습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> badCredentialsExceptionHandler(BadCredentialsException e) {
        String exceptionMessage = e.getMessage();
        log.error("[BadCredentialsExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiResponse<String> exceptionHandler(Exception e) {
        String exceptionMessage = e.getMessage();
        log.error("[Exception] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }
}
