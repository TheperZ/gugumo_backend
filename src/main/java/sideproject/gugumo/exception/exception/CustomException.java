package sideproject.gugumo.exception.exception;

import lombok.Getter;
import sideproject.gugumo.response.StatusCode;

@Getter
public class CustomException extends RuntimeException {

    private final StatusCode statusCode;


    public CustomException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.statusCode = statusCode;
    }
}
