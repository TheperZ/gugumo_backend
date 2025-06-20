package sideproject.gugumo.exception.exception;

import sideproject.gugumo.response.StatusCode;

public class CustomException extends RuntimeException {

    private final StatusCode statusCode;



    public CustomException(StatusCode statusCode) {
        super(statusCode.getCustomMessage());
        this.statusCode = statusCode;
    }
}
