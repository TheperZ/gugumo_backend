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
    @ExceptionHandler(value = {PostNotFoundException.class})
    public ApiResponse<String> handlePostNotFoundException(PostNotFoundException e) {
        log.error("[handlePostNotFoundException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {CommentNotFoundException.class})
    public ApiResponse<String> handleCommentNotFoundException(CommentNotFoundException e) {
        log.error("[handleCommentNotFoundException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {MeetingNotFoundException.class})
    public ApiResponse<String> handleMeetingNotFoundException(MeetingNotFoundException e) {
        log.error("[handleMeetingNotFoundException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {BookmarkNotFoundException.class})
    public ApiResponse<String> handleBookmarkNotFoundException(BookmarkNotFoundException e) {
        log.error("[handleBookmarkNotFoundException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {NoAuthorizationException.class})
    public ApiResponse<String> handleNoAuthorizationException(NoAuthorizationException e) {
        log.error("[handleNoAuthorizationException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DuplicateBookmarkException.class})
    public ApiResponse<String> handleDuplicateBookmarkException(DuplicateBookmarkException e) {
        log.error("[handleDuplicateBookmarkException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[handleMethodArgumentNotValidException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getFieldError().getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotificationNotFoundException.class)
    public ApiResponse<String> handleNofiticationNotFoundException(NotificationNotFoundException e) {
        log.error("[handleMethodArgumentNotValidException] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler()
    public ApiResponse<String> duplicateEmailExceptionHandler(DuplicateEmailException e) {
        String exceptionMessage = e.getMessage();
        log.error("[duplicateEmailExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidExceptionHandler] ex : 입력이 올바르지 못합니다.");
        return ApiResponse.createFail("입력이 올바르지 못합니다.");
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
    public ApiResponse<String> noAuthorizationExceptionHandler(NoAuthorizationException e) {
        String exceptionMessage = e.getMessage();
        log.error("[NoAuthorizationExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> userNotFoundExceptionHandler(UserNotFoundException e) {
        String exceptionMessage = e.getMessage();
        log.error("[UserNotFoundExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiResponse<String> duplicateNicknameExceptionHandler(DuplicateNicknameException e) {
        String exceptionMessage = e.getMessage();
        log.error("[DuplicateNicknameExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> badCredentialsExceptionHandler(BadCredentialsException e) {
        String exceptionMessage = e.getMessage();
        log.error("[BadCredentialsExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }
}
